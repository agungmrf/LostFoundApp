<?php

header("Content-Type: application/json; charset=UTF-8");

require_once 'connect.php';

$key = $_POST['key'];
$id = $_POST['id'];
$picture = $_POST['picture'];

if ( $key == "delete" ){

    $query = "DELETE FROM log_barang WHERE id='$id' ";

        if ( mysqli_query($conn, $query) ){

            $iparr = split ("/", $picture);
            $picture_split = $iparr[5];

            if ( unlink("penyimpanan/".$picture_split) ){

                $result["value"] = "1";
                $result["message"] = "Berhasil dihapus!";

                echo json_encode($result);
                mysqli_close($conn);

            } else {

                $response["value"] = "0";
                $response["message"] = "Error to delete a image! ".mysqli_error($conn);
                echo json_encode($response);

                mysqli_close($conn);
            }

        }
        else {

            $response["value"] = "0";
            $response["message"] = "Error! ".mysqli_error($conn);
            echo json_encode($response);

            mysqli_close($conn);
        }

} else {
    $response["value"] = "0";
    $response["message"] = "Error! ".mysqli_error($conn);
    echo json_encode($response);

    mysqli_close($conn);
}

?>