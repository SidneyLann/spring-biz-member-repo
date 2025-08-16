package com.blockchain.common.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CommUtil {
  public static void sleep(int second) {
    try {
      Thread.sleep(second * 1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<String> getAllLocalIpv6s() {
    List<String> allIps = new ArrayList<>();
    try {
      Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
      while (interfs.hasMoreElements()) {
        NetworkInterface interf = interfs.nextElement();
        Enumeration<InetAddress> addres = interf.getInetAddresses();
        while (addres.hasMoreElements()) {
          InetAddress in = addres.nextElement();
          if (in instanceof Inet6Address) {
            String ipv6 = in.getHostAddress();
            if (ipv6.startsWith("240"))
              allIps.add(ipv6);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return allIps;
  }
}
