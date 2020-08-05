provider "aws" {
  region = var.aws_zone
}

module "s3" {
  source = "../modules/s3"
  bucket_name = var.bucket_name
}

module "ecs-cluster" {
  source = "../modules/ecs/cluster"
  cluster_name = var.cluster_name
}

module "task-definition" {
  source = "../modules/ecs/task-definition"
  aws_zone = var.aws_zone
  bucket_name = module.s3.bucket_name
  container-cpu = var.container_cpu_units
  container-memory = var.container_cpu_units
  image_name = module.ecr_image.ecr_image_url
  name = var.task_definition_name
  task-cpu = var.task_cpu_units
  task-memory = var.task_memory_units
}

module "ecs-service" {
  source = "../modules/ecs/service"
  desired_count = 1
  ecs-cluster-id = module.ecs-cluster.id
  launch-type = "FARGATE"
  service-name = var.service_name
  task-definition-arn = module.task-definition.arn
}

module "ecr" {
  source = "../modules/ecr"
  name = var.ecr_name
}

module "ecr_image" {
  source = "github.com/byu-oit/terraform-aws-ecr-image?ref=v1.0.1"
  dockerfile_dir = "${path.module}/../../."
  ecr_repository_url = module.ecr.url
}

