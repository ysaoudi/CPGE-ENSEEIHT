const mailgun = require("mailgun-js");
const apiKey = process.env.MG_API_KEY;
const domain = process.env.MG_API_DOMAIN
const mg = mailgun({apiKey, domain});


const sendWelcomeEmail = (email, name) => {
    const data = {
        from: 'younes.saoudi@outlook.fr',
        to: email,
        subject: 'Thanks for joining us!',
        text: `Welcome to the app, ${name}. Let me know how you get along with the app.`
    };
    mg.messages().send(data, function (error, body) {
	    console.log(body);
    })
}

const sendCancelationEmail = (email, name) => {
    const data = {
        from: 'younes.saoudi@outlook.fr',
        to: email,
        subject: 'Sorry to see you go..',
        text: `Goodbye, ${name}. I hope to see you back sometime soon.`
    };
    mg.messages().send(data, function (error, body) {
        console.log(body);
    })
}

module.exports = {sendWelcomeEmail, sendCancelationEmail}