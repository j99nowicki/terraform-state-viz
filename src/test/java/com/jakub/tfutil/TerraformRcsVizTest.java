package com.jakub.tfutil;

import org.junit.Test;

import com.jakub.tfutil.TerraformRcsViz;

import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class TerraformRcsVizTest {

	TerraformRcsViz terraformRcsViz = new TerraformRcsViz();	

	@Test
	public void testMain() throws IOException {
		terraformRcsViz.main(null);
		//assertEquals(message, helloWorld.welcomeMessage());     
   }

}

