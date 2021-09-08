<?php

include 'koneksi.php';

$key = $_POST['key'];

$nama = $_POST['nama'];
$jenis = $_POST['jenis'];
$ditemukan = $_POST['ditemukan'];
$keterangan = $_POST['keterangan'];
$tgl_ditemukan = $_POST['tgl_ditemukan'];
$status = $_POST['status'];
$picture = $_POST['picture'];

if ( $key == "insert" ){

    $tgl_ditemukan_newformat = date('Y-m-d', strtotime($tgl_ditemukan));

    $query = "INSERT INTO barang (nama,jenis,ditemukan,keterangan,tgl_ditemukan,status)
    VALUES ('$nama', '$jenis', '$ditemukan', '$keterangan', '$tgl_ditemukan_newformat', '$status') ";

        if ( mysqli_query($conn, $query) ){

            if ($picture == null) {

                $finalPath = "/lost_items/add_image.jpeg";
                $result["value"] = "1";
                $result["message"] = "Berhasil ditambahkan";

                echo json_encode($result);
                mysqli_close($conn);

            } else {

                $id = mysqli_insert_id($conn);
                $path = "penyimpanan/$id.jpeg";
                $finalPath = "/lost_items/".$path;

                $insert_picture = "UPDATE barang SET picture='$finalPath' WHERE id='$id' ";

                if (mysqli_query($conn, $insert_picture)) {

                    if ( file_put_contents( $path, base64_decode($picture) ) ) {

                        $result["value"] = "1";
                        $result["message"] = "Berhasil ditambahkan";

                        echo json_encode($result);
                        mysqli_close($conn);

                    } else {

                        $response["value"] = "0";
                        $response["message"] = "Error! ".mysqli_error($conn);
                        echo json_encode($response);

                        mysqli_close($conn);
                    }

                }
            }

        }
        else {
            $response["value"] = "0";
            $response["message"] = "Error! ".mysqli_error($conn);
            echo json_encode($response);

            mysqli_close($conn);
        }
}

?>