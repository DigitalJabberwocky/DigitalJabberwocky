<?php

// Create image instances
$src = imagecreatefrompng('./1_3mviba5dpjsaiffp4m6kqcvtc7.png');
$dest = imagecreatetruecolor(800, 10);

// Copy
imagecopy($dest, $src, 0, 0, 0, 590, 800, 50);

// Output and free from memory
header('Content-Type: image/png');
imagepng($dest);

imagedestroy($dest);
imagedestroy($src);





?>