<?php
/**
*   @file history.php
*   @brief Classe contenant les setteur et les getteurs
*   @details Date de modification   :   30/09/2014
*   @author     Mathias Da Costa
*   @date       30/09/2014
*/

class History
{
	private $idUser;
	private $location;
	private $dateLastThrow;
	

	
	/*************************************************
					constructor
	*************************************************/
	/*constructeur par defaut*/
    public function __construct()
    {
		$this->idUser="";
		$this->location="";
		$this->dateLastThrow="";

    }
	
	/***********************************************
						getters
	************************************************/
	public function getIdUser()
	{
		return $this->idUser;
	}
	public function getLocation()
	{
		return $this->location;
	}
		public function getDateLastThrow()
	{
		return $this->dateLastThrow;
	}
	/***********************************************
						setters
	************************************************/
	public function setIdUser($idUsr)
	{
		$this->idUser=$idUsr;
	}
	public function setLocation($location)
	{
		$this->location=$location;
	}
		public function setDateLastThrow($dtLstThrw)
	{
		$this->dateLastThrow=$dtLstThrw;
	}
}

?>