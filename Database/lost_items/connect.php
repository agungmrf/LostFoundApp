<?php

$host = 'localhost';
$user = 'root';
$pass = '';
$db = 'lostitems_db';

/* Membuat koneksi ke database */
$conn = mysqli_connect( "$host", "$user", "$pass", "$db" );

/* Pastikan koneksi berhasil */
if ( mysqli_connect_errno() ) {
    printf( "Tidak dapat terhubung ke database: %s\n",
    mysqli_connect_error() );
    exit();
}
?>