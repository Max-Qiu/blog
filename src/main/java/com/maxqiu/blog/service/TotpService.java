package com.maxqiu.blog.service;

import org.springframework.stereotype.Service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

/**
 * TOTP 两步验证服务
 *
 * @author Max_Qiu
 */
@Service
public class TotpService {

    /**
     * 生成 TOTP 密钥
     *
     * @return TOTP 密钥字符串
     */
    public String generateSecret() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    /**
     * 验证 TOTP 验证码
     *
     * @param secret TOTP 密钥
     * @param code 用户输入的6位验证码
     * @return 验证是否成功
     */
    public boolean verifyCode(String secret, String code) {
        if (secret == null || secret.isEmpty() || code == null || code.isEmpty()) {
            return false;
        }

        TimeProvider timeProvider = new SystemTimeProvider();
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        // 验证码，允许时间偏差1个周期（30秒）
        return verifier.isValidCode(secret, code);
    }

    /**
     * 生成二维码 URL（用于 Google Authenticator 扫描）
     *
     * @param username 用户名
     * @param secret TOTP 密钥
     * @param issuer 发行者名称（应用名称）
     * @return 二维码 URL (otpauth://...)
     */
    public String generateQrCodeUrl(String username, String secret, String issuer) {
        QrData data = new QrData.Builder()
            .label(username)
            .secret(secret)
            .issuer(issuer)
            .algorithm(HashingAlgorithm.SHA1)
            .digits(6)
            .period(30)
            .build();

        return data.getUri();
    }

    /**
     * 生成二维码图片的 Base64 编码（可直接在 HTML 中显示）
     *
     * @param username 用户名
     * @param secret TOTP 密钥
     * @param issuer 发行者名称（应用名称）
     * @return Base64 编码的 PNG 图片
     * @throws QrGenerationException 二维码生成异常
     */
    public String generateQrCodeImageBase64(String username, String secret, String issuer) throws QrGenerationException {
        QrData data = new QrData.Builder()
            .label(username)
            .secret(secret)
            .issuer(issuer)
            .algorithm(HashingAlgorithm.SHA1)
            .digits(6)
            .period(30)
            .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);

        // 转换为 Base64
        return java.util.Base64.getEncoder().encodeToString(imageData);
    }

}
