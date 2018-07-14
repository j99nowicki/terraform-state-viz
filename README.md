# terraform-state-viz
Visualise Terraform state file with Graphviz diagram.
Supported Provider: AWS.

* Resources (shown using solid lines):
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
  - aws_vpc (Multiple VPCs can be shown in a single diagram)
  - aws_vpc_endpoint_route_table_association
  - aws_vpc_dhcp_options
  - aws_vpc_dhcp_options_association
  - aws_elasticache_subnet_group
  - aws_redshift_subnet_group

* Data (shown using dashed lines):
  - aws_subnet_ids
  - aws_vpc
  - aws_ami

* Other:
 - AWS availability zones

![Sample 2 VPC with 2 zones and all details](https://github.com/j99nowicki/terraform-state-viz/blob/master/doc/sample_2_vpc2zones_show_all.png?raw=true)

## Usage
```
    java -jar terraform-state-viz-jar-with-dependencies.jar -i <input> [-o <output>]
        -i <input>    Input file name: tfstate file
        -o <output>   Output file name
```
The output graph with graphiviz. For example as input to http://viz-js.com/

## Build, test and package

Prerequisites:
 - git
 - JDK8
 - Maven
 
1. Clone source code
```
    git clone https://github.com/j99nowicki/terraform-state-viz.git
```    
2. Build and package  
```
    mvn clean package
```    
3. Run
```
    cd target
    java -jar terraform-state-viz-jar-with-dependencies.jar -i <input> [-o <output>]
```    
## Importing source code to eclipse

1. Clone source code
```
    git clone https://github.com/j99nowicki/terraform-state-viz.git
```
2. Create eclipse project with maven
```
    mvn eclipse:eclipse
```    
3. In eclipse, go to File->Import, locate the folder and import as a new project

