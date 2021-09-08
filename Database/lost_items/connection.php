<?php

$conn = null;

try{
    //Config
    $host = "localhost";
    $username = "root";
    $password = "";
    $dbname = "lostitems_db";

    //Connect
    $database = "mysql:dbname=$dbname;host=$host";
    $conn = new PDO($database, $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

} catch (PDOException $e){
    echo "Error ! " . $e->getMessage();
    die;
}

?>