<?php

/**
*   @file city.php
*   @brief Classe contenant les setteur et les getteurs
*   @details Date de modification   :   30/09/2014
*   @author     Mathias Da Costa
*   @date       30/09/2014
*/

class City
{
	private $idCity;
	private $name;
	
	/**********************************************************
                       construtor    **********************************************************/
     
    /*constructeur par defaut*/
    public function __construct()
    {
         $this->idCity="";
		 $this->name="";
    }
	/***********************************************
						getters
	************************************************/
	public function getIdCity()
	{
		return $this->IdCity;
	}
	
	public function getName()
	{
		return $this->name;
	}
	
	
	/***********************************************
						setters
	************************************************/
	public function setIdCity($id)
	{
		 $this->IdCity=$id;
	}
	
	public function setName($name)
	{
		$this->name=$name;		
			
	}
	
}
?>