<?php
require_once 'koneksi.php';

$username = $_POST['username'];
$password = $_POST['password'];

$response = []; //Data Response

//Cek username didalam databse
$userQuery = mysqli_query($conn, "SELECT * FROM user WHERE `username`= '".$username. "'");
$row = mysqli_fetch_assoc($userQuery);


if (mysqli_num_rows($userQuery) == 0) {
    $response['status']=false;
    $response['message']='Username Tidak Terdaftar';
 }
else {
    $passwordDB = $row['password'];
    if(strcmp(md5($password),$passwordDB) === 0){
    $response['status']=true;
    $response['message']='Login Berhasil';
    $response['data'] = [
        'user_id' => $row['id'],
        'username' => $row['username'],
        'name' => $row['name'],
        'email' => $row['email']
    ];
} else {
    $response['status'] = false;
    $response['message'] = "Password anda salah";
}
}
$json = json_encode($response,JSON_PRETTY_PRINT);
echo $json;



