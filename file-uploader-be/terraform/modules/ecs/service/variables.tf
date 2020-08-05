variable "service-name" {
  type = string
  description = "Name of the ecs service"
}

variable "ecs-cluster-id" {
  type = number
  description = "Id of the ecs cluster"
}

variable "task-definition-arn" {
  type = string
  description = "ARN of the task definition"
}

variable "desired_count" {
  type = number
  description = "Number of desired instances of service"
}
