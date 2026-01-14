package com.maxqiu.blog;

import com.maxqiu.blog.service.TotpService;

import dev.samstevens.totp.exceptions.QrGenerationException;

/**
 * 生成 TOTP 密钥
 * 
 * 使用该测试类生成 TOTP 密钥，然后手工保存到数据库 user 表的 totp_secret 字段
 *
 * @author Max_Qiu
 */
public class GenerateTotpSecret {

    /**
     * 应用名称（发行者）- 在 Google Authenticator 中显示
     */
    private static final String ISSUER = "MaxQiu Blog";

    /**
     * 用户名 - 需要绑定 TOTP 的用户名
     */
    private static final String USERNAME = "admin";

    public static void main(String[] args) throws QrGenerationException {
        // 创建 TotpService 实例
        TotpService totpService = new TotpService();

        // 1. 生成 TOTP 密钥
        String secret = totpService.generateSecret();

        System.out.println("=".repeat(80));
        System.out.println("TOTP 密钥生成成功！");
        System.out.println("=".repeat(80));
        System.out.println();

        // 2. 输出密钥（需要手工保存到数据库）
        System.out.println("【1】请将以下密钥保存到数据库 user 表的 totp_secret 字段：");
        System.out.println();
        System.out.println("    " + secret);
        System.out.println();
        System.out.println("    SQL 示例：");
        System.out.println("    UPDATE user SET totp_secret = '" + secret + "' WHERE username = '" + USERNAME + "';");
        System.out.println();
        System.out.println("-".repeat(80));
        System.out.println();

        // 3. 生成二维码 URL
        String qrCodeUrl = totpService.generateQrCodeUrl(USERNAME, secret, ISSUER);
        System.out.println("【2】二维码 URL（可以使用在线二维码生成工具转换为图片）：");
        System.out.println();
        System.out.println("    " + qrCodeUrl);
        System.out.println();
        System.out.println("    在线二维码生成工具推荐：");
        System.out.println("    - https://cli.im/url（草料二维码）");
        System.out.println("    - https://www.qrcode-monkey.com/");
        System.out.println();
        System.out.println("-".repeat(80));
        System.out.println();

        // 4. 生成二维码图片（Base64 编码）
        String base64Image = totpService.generateQrCodeImageBase64(USERNAME, secret, ISSUER);
        System.out.println("【3】二维码图片（Base64 编码，可直接在浏览器中显示）：");
        System.out.println();
        System.out.println("    在浏览器地址栏输入以下内容即可查看二维码：");
        System.out.println();
        System.out.println("    data:image/png;base64," + base64Image);
        System.out.println();
        System.out.println("-".repeat(80));
        System.out.println();

        // 5. 使用说明
        System.out.println("【4】使用说明：");
        System.out.println();
        System.out.println("    1. 在手机上下载 Google Authenticator（谷歌身份验证器）");
        System.out.println("       - Android: 在 Google Play 商店搜索 \"Google Authenticator\"");
        System.out.println("       - iOS: 在 App Store 搜索 \"Google Authenticator\"");
        System.out.println("       - 国内用户也可以使用：微软 Authenticator、Authy 等类似应用");
        System.out.println();
        System.out.println("    2. 打开 Google Authenticator，点击 \"+\" 按钮添加账号");
        System.out.println();
        System.out.println("    3. 选择 \"扫描二维码\" 或 \"输入设置密钥\"");
        System.out.println("       - 扫描二维码：使用上面的二维码 URL 生成图片后扫描");
        System.out.println("       - 输入密钥：手动输入上面的 TOTP 密钥（secret）");
        System.out.println();
        System.out.println("    4. 添加成功后，应用会显示一个 6 位数字的动态验证码");
        System.out.println("       这个验证码每 30 秒自动刷新一次");
        System.out.println();
        System.out.println("    5. 登录时输入这个 6 位验证码即可完成两步验证");
        System.out.println();
        System.out.println("=".repeat(80));
    }

}
