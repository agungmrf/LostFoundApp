<?php

header("Content-Type: application/json; charset=UTF-8");

require_once 'koneksi.php';

$key = $_POST['key'];

if ( $key == "update" ){

    $id = $_POST['id'];
    $nama = $_POST['nama'];
    $jenis = $_POST['jenis'];
    $ditemukan = $_POST['ditemukan'];
    $keterangan = $_POST['keterangan'];
    $status = $_POST['status'];
    $tgl_ditemukan = $_POST['tgl_ditemukan'];
    $picture = $_POST['picture'];

    $tgl_ditemukan =  date('Y-m-d', strtotime($tgl_ditemukan));

    $query = "UPDATE barang SET
    nama='$nama',
    jenis='$jenis',
    ditemukan='$ditemukan',
    keterangan='$keterangan',
    status='$status',
    tgl_ditemukan='$tgl_ditemukan'
    WHERE id='$id' ";

        if ( mysqli_query($conn, $query) ){

            if ($picture == null) {

                $result["value"] = "1";
                $result["message"] = "Berhasil diupdate";

                echo json_encode($result);
                mysqli_close($conn);

            } else {

                $path = "penyimpanan/$id.jpeg";
                $finalPath = "/lost_items/".$path;

                $insert_picture = "UPDATE barang SET picture='$finalPath' WHERE id='$id' ";

                if (mysqli_query($conn, $insert_picture)) {

                    if ( file_put_contents( $path, base64_decode($picture) ) ) {

                        $result["value"] = "1";
                        $result["message"] = "Berhasil diupdate!";

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