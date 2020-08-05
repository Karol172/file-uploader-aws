resource "aws_ecs_service" "ecs-service" {
  name            = var.service-name
  cluster         = var.ecs-cluster-id
  task_definition = var.task-definition-arn
  desired_count   = var.desired_count
  deployment_minimum_healthy_percent = 100
  deployment_maximum_percent = 200
}
