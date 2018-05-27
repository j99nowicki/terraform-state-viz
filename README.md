# terraform-state-viz
Visualise Terraform state with Graphviz
Supported Provider: AWS
Shows multiple VPCs in single diagram.

* Resources:
  - aws_instance
  - aws_vpc_endpoint
  - aws_security_group
  - aws_security_group_rule
  - aws_vpn_gateway
  - aws_eip
  - aws_internet_gateway
  - aws_nat_gateway
  - aws_route
  - aws_route_table
  - aws_route_table_association
  - aws_subnet
  - aws_vpc
  - aws_vpc_endpoint_route_table_association
  - aws_vpc_dhcp_options
  - aws_vpc_dhcp_options_association
  - aws_elasticache_subnet_group
  - aws_redshift_subnet_group

* Data:
  - aws_subnet_ids
  - aws_vpc
  - aws_ami

![Sample 2 VPC with 2 zones and all details](https://github.com/j99nowicki/terraform-state-viz/blob/master/doc/sample_2_vpc2zones_show_all.png?raw=true)

## Build and test
    mvn clean package

## Package
    mvn assembly:single

## Run
    cd target
    java -jar terraform-state-viz-jar-with-dependencies.jar

## Create eclipse project
    mvn eclipse:eclipse

Then import project to eclipse
