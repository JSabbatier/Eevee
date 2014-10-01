<?php
/**
*   @file departement.php
*   @brief Classe contenant les setteur et les getteurs
*   @details Date de modification   :   30/09/2014
*   @author     Mathias Da Costa
*   @date       30/09/2014
*/

class Country
{
	private $idDepartement;
	private $name;
	
	/*************************************************
					constructor
	*************************************************/
	/*constructeur par defaut*/
    public function __construct()
    {
         $this->idDepartement="";
		 $this->name="";
    }
	/***********************************************
						getters
	************************************************/
	public function getIdDepartment()
	{
		return $this->idDepartement;
	}
	
	public function getName()
	{
		return $this->name;
	}
	
	
	/***********************************************
						setters
	************************************************/
	public function setIdDepartment($id)
	{
		 $this->idDepartement=$id;
	}
	
	public function setName($name)
	{
		$this->name=$name;		
			
	}



}
?>