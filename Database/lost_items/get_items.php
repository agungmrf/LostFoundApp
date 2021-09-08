<?php

header("Content-Type: application/json; charset=UTF-8");

require_once 'connect.php';

$query = "SELECT * FROM barang ORDER BY id DESC ";
$result = mysqli_query($conn, $query);
$response = array();

$server_name = $_SERVER['SERVER_ADDR'];

while( $row = mysqli_fetch_assoc($result) ){

    array_push($response,
    array(
        'id' =>$row['id'],
        'nama' =>$row['nama'],
        'jenis' =>$row['jenis'],
        'ditemukan' =>$row['ditemukan'],
        'keterangan' =>$row['keterangan'],
        'tgl_ditemukan' =>date('d M Y', strtotime($row['tgl_ditemukan'])),
        'status' =>$row['status'],
        'picture' =>"http://$server_name" . $row['picture'])
    );
}

echo json_encode($response);

mysqli_close($conn);

?>