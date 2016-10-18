<?php

ini_set('upload_max_filesize', '20M');
ini_set('post_max_size', '20M');
ini_set('max_input_time', 1000);
ini_set('max_execution_time', 1000);

$target_dir = "uploads/";
$target_file_name = $target_dir .basename($_FILES["file"]["name"]);
$response = array();

if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name))
 {
  $success = true;
  $message = "Successfully Uploaded";
 }
 else
 {
  $success = false;
  $message = "Error while uploading";
 }


$response["success"] = $success;
$response["message"] = $message;
echo json_encode($response);

?>
