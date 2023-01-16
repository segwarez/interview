locals {
  owner       = var.business_division
  environment = var.environment
  name        = "${var.business_division}-${var.environment}"
  common_tags = {
    owner       = local.owner
    environment = local.environment
  }
  eks_cluster_name = "${local.name}-${var.cluster_name}"
}

provider "aws" {
  region  = var.aws_region
  profile = "default"
}

data "aws_availability_zones" "available" {
}

