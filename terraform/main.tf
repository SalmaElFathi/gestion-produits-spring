terraform {
  required_version = ">= 1.3.0"

  required_providers {
    null = {
      source  = "hashicorp/null"
      version = "~> 3.2"
    }
  }
}

########################################
# 1️⃣ Création des VMs via Vagrant
########################################

resource "null_resource" "vagrant_up" {
  triggers = {
    vagrantfile_hash = filesha256("${path.module}/Vagrantfile")
  }

  provisioner "local-exec" {
    command     = "vagrant up"
    working_dir = path.module
  }
}

########################################
# 2️⃣ Provisionnement Ansible
########################################

resource "null_resource" "ansible" {
  depends_on = [null_resource.vagrant_up]

  provisioner "local-exec" {
    command = <<EOT
      ansible-playbook -i ../ansible/inventory ../ansible/docker.yml
      ansible-playbook -i ../ansible/inventory ../ansible/k8s-common.yml
      ansible-playbook -i ../ansible/inventory ../ansible/k8s-master.yml
      ansible-playbook -i ../ansible/inventory ../ansible/k8s-workers.yml
    EOT
  }
}
