package demo.sicau.datamanagementplatform.util;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 12:37 2018/11/8
 * @Description: 验证码生成工具
 */
public class VerifyCodeUtil {
    private static final long serialVersionUID = -5051097528828603895L;

    /**
         * 验证码图片的宽度。
         */
    private int width = 140;
    /**
         *  验证码图片的高度。
         */
    private int height = 40;
    /**
         * 验证码字符个数
         */
    private int codeCount = 4;
    /**
         * 字体高度   
         */
    private int fontHeight = 25;
    /**
         * 第一个字符的x轴值，因为后面的字符坐标依次递增，所以它们的x轴值是codeX的倍数
         */
    private int codeX = 24;
    /**
         * codeY ,验证字符的y轴值，因为并行所以值一样
         */
    private int codeY = 30;
    /**
         * codeSequence 表示字符允许出现的序列值
         */
    char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };



    /**
         * @param request
         * @param response
         * @throws ServletException
         * @throws java.io.IOException
         */
    public String createCredentialImg( HttpServletResponse response,String verifyCodeKey) throws ServletException, java.io.IOException {
        // 定义图像buffer   
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();
        // 创建一个随机数生成器类   
        Random random = new Random();
        // 将图像填充为白色   
        gd.setColor(Color.white);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。   
        Font font = new Font("Fixedsys", Font.ITALIC, fontHeight);
        // 设置字体。   
        gd.setFont(font);
        // 画边框。   
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。   
        gd.setColor(Color.gray);
        for (int i = 0; i < 16; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。   
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。   
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
//            red = random.nextInt(255);
//            green = random.nextInt(255);
//            blue = random.nextInt(255);
// 用随机产生的颜色将验证码绘制到图像中.
            gd.setColor(new Color(red,green,blue));
            gd.drawString(strRand, (i + 1) * codeX, (float) (25 + 14 * Math.random()));
// 将产生的四个随机数组合在一起。   
            randomCode.append(strRand);
        }
        // 禁止图像缓存。   
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Verify-Code-Key", verifyCodeKey);
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。   
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
        return randomCode.toString();
    }
}
