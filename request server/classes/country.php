<?php
/**
*   @file country.php
*   @brief Classe contenant les setteur et les getteurs
*   @details Date de modification   :   30/09/2014
*   @author     Mathias Da Costa
*   @date       30/09/2014
*/

class Country
{
	private $idCountry;
	private $name;
	
	/*************************************************
					constructor
	*************************************************/
	/*constructeur par defaut*/
    public function __construct()
    {
         $this->idCountry="";
		 $this->name="";
    }
	/***********************************************
						getters
	************************************************/
	public function getIdCountry()
	{
		return $this->idCountry;
	}
	
	public function getName()
	{
		return $this->name;
	}
	
	
	/***********************************************
						setters
	************************************************/
	public function setIdCountry($id)
	{
		 $this->idCountry=$id;
	}
	
	public function setName($name)
	{
		$this->name=$name;		
			
	}



}
?>