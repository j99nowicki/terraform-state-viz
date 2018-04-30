package com.jakub.tfutil;

import org.junit.Test;

import java.io.IOException;

public class TerraformStateVizTest {

	TerraformStateViz terraformStateViz = new TerraformStateViz();	

	@Test
	public void testMain() throws IOException {
		terraformStateViz.main(null);
		//assertEquals(message, helloWorld.welcomeMessage());     
   }

}

