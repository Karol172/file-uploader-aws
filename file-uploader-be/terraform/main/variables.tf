variable "bucket_name" {
  default = "file-uploader-bucket"
}

variable "cluster_name" {
  default = "file-uploader-cluster"
}

variable "aws_zone" {
  default = "eu-central-1"
}

variable "container_cpu_units" {
  default = 256
}

variable "task_definition_name" {
  default = 512
}

variable "task_memory_units" {
  default = 2048
}

variable "task_cpu_units" {
  default = 1024
}

variable "service_name" {
  default = "file-service"
}

variable "ecr_name" {
  default = "file-repository-repo"
}
