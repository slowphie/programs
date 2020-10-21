let monthName = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

function storage() {

	let curDate = new Date();
    let reviewDate = curDate.getDate() + " " +(monthName[curDate.getMonth()]) + " " + curDate.getFullYear();
	let inputReviewText = document.getElementById("review");
    let inputReviewer= document.getElementById("name");
	localStorage.setItem("name"+ curDate, inputReviewer.value + "*" + inputReviewText.value + "*" + reviewDate);

}

function getS(){
	let reviewer = document.getElementById("name").value;
	let review_text = document.getElementById("review").value;

	let curDate = new Date();
    let date = curDate.getDate() + " " +(monthName[curDate.getMonth()]) + " " + curDate.getFullYear();

    return getSWithParameters(reviewer,review_text,date)
}

function getSWithParameters(reviewer, review_text, date){
	return '<p id=\'reviewText\'>' + review_text + '</p><p id=\'reviewerInfo\'>' + '&mdash; ' 
		+ reviewer + ", " + date + "</p>";

}

function post() {
	storage()
 	var block = document.createElement("BLOCKQUOTE");
  	block.innerHTML = getS();
  	document.getElementById("posted").appendChild(block);
  	document.getElementById("reviewForm").reset();
}


function display_all(){
	document.getElementById("postedSubs").innerHTML = "";
	if (localStorage.length==0){
		document.getElementById("postedSubs").innerHTML = "<p>No reviews to show</p>";
	}
	for(let i=0; i<localStorage.length; i++) {
		let block = document.createElement("BLOCKQUOTE");
		let review = localStorage.getItem(localStorage.key(i)).split("*");

  		let reviewer = review[0];
  		let reviewer_text = review[1];
  		let date = review[2];
  		block.innerHTML = getSWithParameters(reviewer,reviewer_text,date);
	  	document.getElementById("postedSubs").appendChild(block);

	}

	
}

function delete_all(){
	localStorage.clear();
	document.getElementById("postedSubs").innerHTML = "<p>Deleted</p>";
}