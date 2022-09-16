<?php
$conn=mysqli_connect("localhost","root","12345");
mysqli_select_db($conn,"pasakay_system");

$user_name = (isset($_POST['user_name'])) ? $_POST['user_name'] : 'defaultUser';

if(mysqli_connect_error())
{ die("cannot connect to database field:". mysqli_connect_error());   }
 // define quesry 

$query="select * from passenger where user_name='".$user_name."'";  // $usename=$_GET['username'];
$result=  mysqli_query($conn, $query);
if(! $result)
{ die("Error in query");}
 //get data from database
//$output=array();
if($row=mysqli_fetch_assoc($result))
{
 echo "Username Already Exist! Registration Failed! Please Try Again and Use Other Username";
 
}else{
	process();
}


// 4 clear
mysqli_free_result($result);
//5- close connection
mysqli_close($conn);

function process(){
	$conn=mysqli_connect("localhost","root","");
	mysqli_select_db($conn,"pasakay_system");

	   $first_name=$_POST['t1'];
	   $middle_name=$_POST['t2'];
	   $last_name=$_POST['t3'];
	   $address=$_POST['t4'];	
	   $email=$_POST['t5'];
	   $gender=$_POST['t6'];	
	   $password=$_POST['t7'];	   
	   $img=$_POST['upload'];
	   $user_name=$_POST['user_name'];
     
                   $filename="IMG".rand().".jpg";
	   file_put_contents("images/".$filename,base64_decode($img));
//first_name,middle_name,last_name,address,email,gender,password,image

			$qry="INSERT INTO passenger (first_name,middle_name,last_name,address,email,gender,password,image,user_name)
			      VALUES ('$first_name','$middle_name','$last_name','$address','$email','$gender','$password','$filename','$user_name')";

			$res=mysqli_query($conn,$qry);
			
			if($res==true)
			echo "Account Successfully Created";
			else
			 echo "Could not upload File";

}
?>
