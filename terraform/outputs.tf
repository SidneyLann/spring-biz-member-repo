output "public_ip_address" {
  description = "Public IP address of the Tomcat VM"
  value       = azurerm_public_ip.main.ip_address
}