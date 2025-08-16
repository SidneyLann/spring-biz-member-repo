package com.blockchain.member.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);
  private static final int targetWidth = 55;
  private static final int targetHeight = 55;
  private static final double circleR = 20;
  private static final double r1 = 10;
  @Value("${sec.validation.image.path}")
  private String originalPath;

  public Integer createImage(Map<String, String> resultMap) {
    try {
      BufferedImage sourceImage = ImageIO.read(new FileInputStream(originalPath));
      double factor = Math.random();
      int X = (int) (sourceImage.getWidth() / 2 * (1 + factor));
      int Y = (int) (sourceImage.getHeight() * factor);
      if (Y - targetHeight < 5) {
        Y = 2 * Y - targetHeight + 5;
      } else if (X + targetWidth + 5 > sourceImage.getWidth()) {
        X = sourceImage.getWidth() - targetWidth - 5;
      } else if (Y + targetHeight + 5 > sourceImage.getHeight()) {
        Y = sourceImage.getHeight() - targetHeight - 5;
      }
      BufferedImage cutoutImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
//      Graphics2D g2d = cutoutImage.createGraphics();
//      cutoutImage = g2d.getDeviceConfiguration().createCompatibleImage(targetWidth, targetHeight, Transparency.TRANSLUCENT);
//      g2d.dispose();
      addShadow4SourceImage(sourceImage, cutoutImage, genCutoutRegion(), X, Y);
      // resultMap.put("topLeftX", "" + X);
      resultMap.put("topLeftY", "" + Y);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ImageIO.write(sourceImage, "jpg", out);
      resultMap.put("backgroundImage", Base64.encodeBase64String(out.toByteArray()));
      out.reset();
      ImageIO.write(cutoutImage, "jpg", out);
      resultMap.put("cutoutImage", Base64.encodeBase64String(out.toByteArray()));
      return X;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private int[][] genCutoutRegion() {
    int[][] data = new int[targetWidth][targetHeight];
    // 随机生成圆的位置
    double x1 = targetWidth - circleR;
    double y1 = circleR + Math.random() * (targetHeight - 3 * circleR - r1);
    double po = circleR * circleR;

    double xbegin = targetWidth - circleR - r1;
    double ybegin = targetHeight - circleR - r1;

    for (int i = 0; i < targetWidth; i++) {
      for (int j = 0; j < targetHeight; j++) {
        double d2 = Math.pow(j - 2, 2) + Math.pow(i - y1, 2);
        double d3 = Math.pow(i - x1, 2) + Math.pow(j - y1, 2);
        if ((j <= ybegin && d2 <= po) || (i >= xbegin && d3 >= po)) {
          data[i][j] = 0;
          LOG.debug(i + " (i,j)=0 " + j);
        } else {
          data[i][j] = 1;
          LOG.debug(i + " (i,j)=1 " + j);
        }
      }
    }
    return data;
  }

  private void addShadow4SourceImage(BufferedImage sourceImage, BufferedImage cutoutImage, int[][] cutoutRegion, int pointX, int pointY) {
    int i = 0;
    while (i < targetWidth) {
      int j = 0;
      while (j < targetHeight) {
        int valRGB = cutoutRegion[i][j];
        int rgb_ori = sourceImage.getRGB(pointX + i, pointY + j);
        // 抠图上复制对应颜色值
        cutoutImage.setRGB(i, j, rgb_ori);
      //  if (valRGB == 1) {
          try {
            // 原图对应位置颜色变化
            sourceImage.setRGB(pointX + i, pointY + j, Color.LIGHT_GRAY.getRGB());
          } catch (ArrayIndexOutOfBoundsException e) {
            if (pointX + i > sourceImage.getWidth())
              LOG.debug(pointX + i + " Coordinate X out of bounds " + sourceImage.getWidth());
            if (pointY + i > sourceImage.getHeight())
              LOG.debug(pointX + j + " Coordinate Y out of bounds " + sourceImage.getHeight());
            throw e;
          }
      //  }
        j++;
      }
      i++;
    }
  }

  private BufferedImage blurryPicture(int radius, boolean horizontal, BufferedImage sourceImage) {
    if (radius < 1) {
      throw new IllegalArgumentException("Radius must be >= 1");
    }

    int size = radius * 2 + 1;
    float[] data = new float[size];

    float sigma = radius / 3.0f;
    float twoSigmaSquare = 2.0f * sigma * sigma;
    float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
    float total = 0.0f;

    for (int i = -radius; i <= radius; i++) {
      float distance = i * i;
      int index = i + radius;
      data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
      total += data[index];
    }

    for (int i = 0; i < data.length; i++) {
      data[i] /= total;
    }

    Kernel kernel;
    if (horizontal) {
      kernel = new Kernel(size, 1, data);
    } else {
      kernel = new Kernel(1, size, data);
    }
    BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    BufferedImage destImage = new BufferedImage(sourceImage.getHeight(), sourceImage.getWidth(), BufferedImage.TYPE_4BYTE_ABGR);
    op.filter(sourceImage, destImage);

    return destImage;
  }

  private String compressPicture(BufferedImage sourceImage, String imagType) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // 得到指定Format图片的writer
    Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(imagType);
    ImageWriter writer = (ImageWriter) iter.next();
    // 获取指定writer的输出参数设置(ImageWriteParam )
    ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
    imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
    imageWriteParam.setCompressionQuality(1f); // 设置压缩质量参数
    imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
    ColorModel colorModel = ColorModel.getRGBdefault();
    // 指定压缩时使用的色彩模式
    imageWriteParam.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
    writer.setOutput(ImageIO.createImageOutputStream(bos));
    IIOImage iIamge = new IIOImage(sourceImage, null, null);
    writer.write(null, iIamge, imageWriteParam);

    return Base64.encodeBase64String(bos.toByteArray());
  }
}
