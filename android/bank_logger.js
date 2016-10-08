banks_login = { Tangerine: {
					login_url: "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA",
					inputs_id: ["ACN", "Answer", "PIN"],
					},
			    Desjardins: {
			    	login_url: "https://accweb.mouv.desjardins.com/identifiantunique/identification",
			    	inputs_id: ["codeUtilisateur", "question", "motDePasse"],
			    }}

user_info = { username: "user",
			  password: "******"
			}

bank_name = "Desjardins"
bank = banks_login[bank_name]
window.location.href = bank.login_url;



// *** First login screen ***
input = $('#' + bank.inputs_id[0])

input.val(user_info.username)
// document.getElementById("cbRemember").checked = true

input.closest("form").submit()


if (bank_name == "Tangerine") {
	// *** Second screen *** // Not tested with Desjardins!
	q_ans = {"Quel était le nom de votre premier animal de compagnie ?": "&^%",
			 "Quel est le nom de votre meilleur(e) ami(e) d'enfance ?": "&^%$"}
	question = $("div.content-main-wrapper .CB_DoNotShow:first").html()

	answer = q_ans[question]

	input = $('#' + bank.inputs_id[1])

	if (answer != undefined) {
		input.val(answer)
	}
	// document.getElementById("Register").checked = true

	input.closest("form").submit()
}


// *** Third screen ***
input = $('#' + bank.inputs_id[2])

input.val(user_info.password)

input.closest("form").submit()
