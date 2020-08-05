variable "name" {
  type = string
  description = "The name of the task definition."
}

variable "container-cpu" {
  type = number
  description = "Number of CPU units for container."
}

variable "container-memory" {
  type = number
  description = "Number of memory units for container."
}
variable "bucket_name" {
  type = string
  description = "The name of S3 bucket."
}

variable "aws_zone" {
  type = string
  description = "AWS zone."
}
variable "task-cpu" {
  type = number
  description = "Number of CPU units for task-definition."
}
variable "task-memory" {
  type = number
  description = "Number of CPU units for task-definition."
}

variable "image_name" {
  type = string
  description = "The name of the image name on aws docker registry."
}