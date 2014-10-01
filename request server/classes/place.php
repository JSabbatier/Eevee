<?php
/**
*   @file place.php
*   @brief Classe contenant les setteur et les getteurs
*   @details Date de modification   :   30/09/2014
*   @author     Mathias Da Costa
*   @date       30/09/2014
*/
class Place
{
	private $idPlace;
	private $location;
	private $idCity;
	private $idDepartment;
	private $idCountry;
	private $name; 
	private $resume;
	private $picture;
	
	/*************************************************
					constructor
	*************************************************/
	/*constructeur par defaut*/
    public function __construct()
    {
		$this->idPlace="";
		$this->location="";
		$this->idCity="";
		$this->idDepartment="";
		$this->idCountry="";
		$this->name="";
        $this->resume="";
		$this->picture="";
    }
	
	/***********************************************
						getters
	************************************************/
	public function getIdplace()
	{
		return $this->idPlace;
	}
	public function getLocation()
	{
		return $this->location;
	}

	public function getIdCity()
	{
		return $this->idCity;
	}

	public function getIdDepartment()
	{
		return $this->idDepartment;
	}

	public function getCountry()
	{
		return $this->idCountry;
	}

	public function getName()
	{
		return $this->name;
	}

	public function getResume()
	{
		return $this->resume;
	}

	public function getPicture()
	{
		return $this->picture;
	}

	/***********************************************
						setters
	************************************************/
	public function setIdplace($idPlace)
	{
		$this->idPlace;
	}
	public function setLocation($location)
	{
		return $this->location;
	}

	public function setIdCity($idCity)
	{
		return $this->idCity;
	}

	public function setIdDepartment($idDepartment)
	{
		return $this->idDepartment;
	}

	public function setCountry($idCountry)
	{
		return $this->idCountry;
	}

	public function setName()
	{
		return $this->name;
	}

	public function setResume()
	{
		return $this->resume;
	}

	public function setPicture()
	{
		return $this->picture;
	} 
}

?>