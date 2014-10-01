<?php
/**
*   @file user.php
*   @brief Classe contenant les setteur et les getteurs
*   @details Date de modification   :   30/09/2014
*   @author     Mathias Da Costa
*   @date       30/09/2014
*/
class User
{
	private $idUser;
	private $name;
	private $psw;
	private $adrMail;
	private $token;
	
	/*************************************************
					constructor
	*************************************************/
	/*constructeur par defaut*/
    public function __construct()
    {
		$this->idUser="";
		$this->name="";
		$this->psw="";
		$this->adrMail="";
		$this->token="";
		
    }
	
	/***********************************************
						getters
	************************************************/
	public function getIdUser()
	{
		return $this->idUser;
	}
	public function getUsrName()
	{
		return $this->name;
	}
	public function getPsw()
	{
		return $this->psw;
	}
	public function getAdrMail()
	{
		return $this->adrMail;
	}
	public function getToken()
	{
		return $this->token;
	}
	/***********************************************
						setters
	************************************************/
	public function setIdUser($idusr)
	{
		$this->idUser=$idusr;
	}
	public function setUsrName($usrName)
	{
		$this->name=$usrName;
	}
	public function setPsw($psw)
	{
		$this->psw=$psw;
	}
	public function setAdrMail($adrMail)
	{
		$this->adrMail=$adrMail;
	}
	public function setToken($tok)
	{
		$this->token=$tok;
	}

	
}

?>