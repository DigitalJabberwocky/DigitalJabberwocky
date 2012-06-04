




function outputImage(image){
	// SEND IMAGE TO SERVER
//	window.open(image, 'Canvas Export','height=400,width=400');
	var data = $.ajax({
		type: "POST",
		url: "server.php",
		data: "i=" + image,
		cache: false,
		async: false
	}).responseText;
}












            //To hold the data for each action on the screen
            var undoHistory = [];

            //Function to save the states in history
            function saveActions() {
                var imgData = document.getElementById("canvas1").toDataURL("image/png");
                undoHistory.push(imgData);
                $('#undo').removeAttr('disabled');
            }

            //Actural Undo Function
            function undoDraw(){
                if(undoHistory.length > 0){
                    var undoImg = new Image();
                    $(undoImg).load(function(){
                        var context = document.getElementById("canvas1").getContext("2d");
                        context.drawImage(undoImg, 0,0);
                    });
                    undoImg.src = undoHistory.pop();
                    if(undoHistory.length == 0)
                        $('#undo').attr('disabled','disabled');
                }                
            }

            //Set the canvas defaults
            //Including a white background
            function canvasInit(){
                context = document.getElementById("canvas1").getContext("2d");
                context.lineCap = "round";
                //Fill it with white background
                context.save();
                context.fillStyle = '#fff';
                context.fillRect(0, 0, context.canvas.width, context.canvas.height);
                context.restore();
				//$('#Page').hide();
				$('#waitForDraw').hide();
				
				if(undoHistory.length == 0)
                        $('#undo').attr('disabled','disabled');
            }

			
			
			
			
			
	
function waitForImage(){
	$('#Page').hide();
	$('#waitForDraw').show();
	
	id = 10;  // STUB

	
	var waiting = $.ajax({
		type: "POST",
		url: "server.php",
		data: "w=1",
		cache: false,
		async: false
	}).responseText;
	
		// CHECK IF THE PLAYER HAS FINISHED DRAWING
		// IF THEY HAVE, waiting = 0;

		
	if(waiting == "DRAW"){
			// SHOW PART OF THE OTHER IMAGE
		    $('#colors li:first').click();
		    $('#brush_size').change();
		    undoHistory = [];
			$('#Page').show();
			canvasInit();
	}else{
		setTimeout(waitForImage, 500);
	}
}	
			
			
			
			
			
			
			

function doIt(){
	var canvas, cntxt, top, left, draw, draw = 0;
	//Get the canvas element
	//var canvas = $("#canvas1");                
	canvas = document.getElementById("canvas1");
	cntxt = canvas.getContext("2d");
	top = $('#canvas1').offset().top;
	left = $('#canvas1').offset().left;
	canvasInit();

	//Drawing Code
	$('#canvas1').mousedown(function(e){
		if(e.button == 0){
			draw = 1;
			//Start The drawing flow
			//Save the state
			saveActions();
			cntxt.beginPath();
			cntxt.moveTo(e.pageX-left, e.pageY-top);
		}
		else{
			draw = 0;
		}
	})
	.mouseup(function(e){
		if(e.button != 0){
			draw = 1;
		}
		else{
			draw = 0;
			cntxt.lineTo(e.pageX-left+1, e.pageY-top+1);
			cntxt.stroke();
			cntxt.closePath();
		}
	})
	.mousemove(function(e){
		if(draw == 1){
			cntxt.lineTo(e.pageX-left+1, e.pageY-top+1);
			cntxt.stroke();
		}
	});

	//Extra Links Code
	$('#export').click(function(e){
		e.preventDefault();
		outputImage(canvas.toDataURL());
		waitForImage();
	});
	
	
	$('#clear').click(function(e){
		e.preventDefault();
		canvas.width = canvas.width;
		canvas.height = canvas.height;
		canvasInit();
		$('#colors li:first').click();
		$('#brush_size').change();
		undoHistory = [];
	});
	$('#brush_size').change(function(e){
		cntxt.lineWidth = $(this).val();
	});            
	$('#colors li').click(function(e){
		e.preventDefault();
		$('#colors li').removeClass('selected');
		$(this).addClass('selected');
		cntxt.strokeStyle = $(this).css('background-color');
	});

	//Undo Binding
	$('#undo').click(function(e){
		e.preventDefault();
		undoDraw()
	});

	//Init the brush and color
	$('#colors li:first').click();
	$('#brush_size').change();

}
			
			
			
// Use this to check for 'friend' connections...  Copy code from WaitForImage
$(function waitForConnections(){
	
	document.onselectstart = function() {return false;} // ie
	//document.onmousedown = function() {return false;} // mozilla
	
	$('#Page').hide();
	$('#waitForDraw').hide();
	var ready = $.ajax({
		type: "POST",
		url: "server.php",
		data: "c=1",
		cache: false,
		async: false
	}).responseText;
	
	if(ready == "DRAW"){
		$('#waitForFriend').hide();
		$('#Page').show();
		doIt();
	}else if(ready == "FRND"){
		$('#waitForFriend').hide();
		waitForImage();
		doIt();
	}else{
		$('#waitForFriend').show();
		setTimeout(waitForConnections, 500);
	}
});