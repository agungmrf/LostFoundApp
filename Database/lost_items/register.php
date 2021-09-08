<?php
include_once 'koneksi.php';

$response = array("status" => FALSE);

$username = filter_input(INPUT_POST, 'username', FILTER_SANITIZE_STRING);
$password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_STRING);
$name     = filter_input(INPUT_POST, 'name', FILTER_SANITIZE_STRING);
$email    = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING);


 $encrypted_password = md5($password);// encrypted password

    $sql = mysqli_query($conn, "SELECT * from user WHERE username = '$username'");

    if(mysqli_num_rows($sql) > 0) {
        $response['status']= FALSE;
        $response['message']='Akun sudah digunakan';

        echo json_encode($response);
    }else{
     $sql = mysqli_query($conn, "INSERT INTO user (username,password, name, email) VALUES ('$username','$encrypted_password','$name','$email')");

     if($sql) {
        $response['status']= TRUE;
        $response['message']='Registrasi Berhasil';
        $response['data'] = [
            'username' => $username,
            'name' => $name,
            'email' => $email
        ];

   echo json_encode($response);
     } else {
      $response["status"] = FALSE;
         $response["message"] = "Registrasi Gagal";

   echo json_encode($response);
     }
}
?>