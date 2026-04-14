terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.1"
    }
  }
}

provider "docker" {}

resource "docker_image" "mongodb_image" {
  name         = "mongo:latest"
  keep_locally = false
}

resource "docker_container" "mongodb" {
  image = docker_image.mongodb_image.image_id
  name  = "mongo_db_test"
  ports {
    internal = 27017
    external = 27017
  }
}