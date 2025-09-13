variable "resource_group_name" {
  description = "The name of the resource group"
  type        = string
}

variable "location" {
  description = "The Azure region to deploy to"
  type        = string
  default     = "koreacentral"
}

variable "vm_name" {
  description = "The name of the virtual machine"
  type        = string
}

variable "client_id" {
  description = "Azure service principal client ID"
  type        = string
  sensitive   = true
}

variable "client_secret" {
  description = "Azure service principal client secret"
  type        = string
  sensitive   = true
}

variable "tenant_id" {
  description = "Azure tenant ID"
  type        = string
  sensitive   = true
}

variable "subscription_id" {
  description = "Azure subscription ID"
  type        = string
  sensitive   = true
}