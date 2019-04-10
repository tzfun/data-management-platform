package demo.sicau.datamanagementplatform.service.imp;

import demo.sicau.datamanagementplatform.dao.ResourceDao;
import demo.sicau.datamanagementplatform.entity.DTO.Resource;
import demo.sicau.datamanagementplatform.entity.DTO.ResourceInfo;
import demo.sicau.datamanagementplatform.entity.POJO.PO.ResourcePO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceArticleVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceListVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.ResourceService;
import demo.sicau.datamanagementplatform.util.*;
import demo.sicau.datamanagementplatform.util.redis.RedisKeyManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:05 2018/11/24
 * @Description:
 */
@Service
public class ResourceServiceImp implements ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImp.class);

    @Value("${server.host}")
    private String host;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ResultUtil resultUtil;
    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public ResultVO uploadResource(ResourceVO resourceVO,String userId) {
        try{
            MultipartFile img = resourceVO.getImg();
            MultipartFile file = resourceVO.getFile();
            // 检查文件和图片的类型
            if (!(fileUtil.checkImgType(img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf(".")+ 1)) && fileUtil.checkResourceType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)))){
                return resultUtil.error(ResultEnum.FILE_FORMAT_WRONG);
            }
            // 检查文件和图片的大小
            if(!(fileUtil.checkImgSize(img.getSize()) && fileUtil.checkResourceSize(file.getSize()))){
                return resultUtil.error(ResultEnum.RESOURCE_SIZE_TOO_BIG);
            }
            // 先将文件保存到本地
            logger.info("上传资源图片：name={},type={},size={}",img.getOriginalFilename(),img.getContentType(),img.getSize()/(1024*1024)+"M");
            logger.info("上传资源文件：name={},type={},size={}",file.getOriginalFilename(),file.getContentType(),file.getSize()/(1024*1024)+"M");
            String imgName = fileUtil.saveFile(img,fileUtil.getArticleImgLocalPath(userId));
            String imgUrl = host + "/" + fileUtil.getArticleImgUrl(userId) + "/" + imgName;
            logger.info("保存资源图片：url={}",imgUrl);

            String resourceName = fileUtil.saveFile(file,fileUtil.getResourceLocalPath(userId));
            String resourceUrl = host + "/" + fileUtil.getResourceUrl(userId) + "/" + resourceName;
            logger.info("保存资源文件：url={}",resourceUrl);

            // 保存资源对象，并对铭感字符进行转义
            ResourceInfo resource = new ResourceInfo();
            resource.setId(resourceVO.getId());
            resource.setIntroduction(StrUtil.strEncode(resourceVO.getIntroduction()));
            resource.setSummary(StrUtil.strEncode(resourceVO.getSummary()));
            resource.setTitle(StrUtil.strEncode(resourceVO.getTitle()));
            resource.setType(StrUtil.strEncode(resourceVO.getType()));
            resource.setImgUrl(imgUrl);
            resource.setFileUrl(resourceUrl);
            resource.setFileName(file.getOriginalFilename());
            resource.setFileSize(file.getSize());
            resource.setFileFullType(file.getContentType());
            resource.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            // 将数据保存到数据库
            if (resourceDao.insertResource(resource,userId)){
                return resultUtil.success(resourceVO.getId());
            }
        }catch (Exception e) {
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Transactional
    @Override
    public ResultVO getById(String id) {
        try{
            // 先从redis数据库中读取是否有该资源，如果有就直接返回，如果没有就从数据库中获取
            Map<Object,Object> resourceMap = redisUtil.hmget(RedisKeyManagerUtil.getResourceKey(id));
            if (resourceMap.isEmpty()){
                ResourceArticleVO resourceArticleVO = resourceDao.selectResourceArticleById(id);
                if (resourceArticleVO != null ){
                    if(resourceDao.updateSeeNum(id)){
                        // 解码字符串
                        ResourcePO resource = resourceArticleVO.getResource();
                        resource.setTitle(StrUtil.strDecode(resource.getTitle()));
                        resource.setSummary(StrUtil.strDecode(resource.getSummary()));
                        resource.setIntroduction(StrUtil.strDecode(resource.getIntroduction()));
                        // 将从数据库中取出的文章加入缓存，有效时间两分钟
                        redisUtil.hmset(RedisKeyManagerUtil.getResourceKey(id), (Map<String, Object>) ObjectTransformUtil.objectToMap(resourceArticleVO),120);
                        return resultUtil.success(resourceArticleVO);
                    }
                }else {
                    return resultUtil.error(ResultEnum.RESOURCE_NOT_FOUND);
                }
            }else {
                return resultUtil.success(resourceMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO getListByType(int page, int pageSize, String type) {
        try{
            ArrayList<ResourceListVO> resourceListVOS = resourceDao.selectResourceListByType(page,pageSize,type);
            if (resourceListVOS!=null){
                for (ResourceListVO resourceListVO : resourceListVOS){
                    ResourceInfo resource = resourceListVO.getResource();
                    resourceListVO.getResource().setTitle(StrUtil.strDecode(resource.getTitle()));
                    resourceListVO.getResource().setSummary(StrUtil.strDecode(resource.getSummary()));
                }
            }
            int total = resourceDao.countResource(type);
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("total",total);
            resMap.put("rows",resourceListVOS);
            return resultUtil.success(resMap);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO getList(int page, int pageSize) {
        try{

            ArrayList<ResourceListVO> resourceListVOS = resourceDao.selectResourceList((page-1)*pageSize,pageSize);
            if (resourceListVOS!=null){
                for (ResourceListVO resourceListVO : resourceListVOS){
                    ResourceInfo resource = resourceListVO.getResource();
                    resourceListVO.getResource().setTitle(StrUtil.strDecode(resource.getTitle()));
                    resourceListVO.getResource().setSummary(StrUtil.strDecode(resource.getSummary()));
                }
            }
            return resultUtil.success(resourceListVOS);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO getDownloadHot(int page, int pageSize) {
        try{
            ArrayList<ResourceListVO> resourceListVOS = resourceDao.selectResourceListOrderByDownload((page-1)*pageSize,pageSize);
            if (resourceListVOS!=null){
                for (ResourceListVO resourceListVO : resourceListVOS){
                    ResourceInfo resource = resourceListVO.getResource();
                    resourceListVO.getResource().setTitle(StrUtil.strDecode(resource.getTitle()));
                    resourceListVO.getResource().setSummary(StrUtil.strDecode(resource.getSummary()));
                }
            }
            return resultUtil.success(resourceListVOS);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO listPersonal(int page, int pageSize, String userId) {
        try{
            ArrayList<ResourcePO> resourcePOS = resourceDao.selectResourceByUserId(page,pageSize,userId);
            int total = resourceDao.selectTotalByUserId(userId);
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("rows",resourcePOS);
            resMap.put("total",total);
            return resultUtil.success(resMap);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO delPersonal(String id, String userId) {
        try{
            ResourcePO resource = resourceDao.selectResourceById(id);
            if (resource == null){
                return resultUtil.error(ResultEnum.RESOURCE_NOT_FOUND);
            }else if(!resource.getUserId().equals(userId)){
                return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
            }else{
                if (resourceDao.deleteByUserIdAndId(id,userId)){
                    String localPath = resource.getFileUrl();
                    if (!fileUtil.delFile(localPath)){
                        logger.warn("删除数据库记录成功，但删除资源文件失败，file_path={}",localPath);
                    }
                }
            }
            return resultUtil.success();
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO listAll(int page, int pageSize) {
        try{
            ArrayList<ResourcePO> resourcePOS = resourceDao.selectAll(page,pageSize);
            int total = resourceDao.countAll();
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("rows",resourcePOS);
            resMap.put("total",total);
            return resultUtil.success(resMap);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO delete(String id) {
        try{
            ResourcePO resource = resourceDao.selectResourceById(id);
            if (resource == null){
                return resultUtil.error(ResultEnum.RESOURCE_NOT_FOUND);
            }else{
                if (resourceDao.deleteById(id)){
                    String localPath = resource.getFileUrl();
                    if (!fileUtil.delFile(localPath)){
                        logger.warn("删除数据库记录成功，但删除资源文件失败，file_path={}",localPath);
                    }
                }
            }
            return resultUtil.success();
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO updatePassStatus(String id, int hasPass) {
        try{
            resourceDao.updatePassStatus(id,hasPass);
            return resultUtil.success();
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }
}
