resource "aws_ecs_task_definition" "task_definition" {
  family = var.name
  container_definitions = templatefile("${path.module}/service.tmpl", {
    container-cpu = var.container-cpu,
    container-memory = var.container-memory,
    log_group = aws_cloudwatch_log_group.ecs_logs.name,
    aws_zone = var.aws_zone,
    bucket_name = var.bucket_name,
    image_name = var.image_name
  })
  cpu = var.task-cpu
  memory = var.task-memory
  task_role_arn = aws_iam_role.ecs-role.arn
  execution_role_arn = aws_iam_role.ecs-exec-task-role.arn
}

resource "aws_cloudwatch_log_group" "ecs_logs" {
  name = var.name
}
