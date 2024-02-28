const express = require('express'),
    app = express(),
    port = process.env.PORT || 5000,
    cors = require('cors'),
    router = express.Router(),
    ORIGIN_URL = 'http://localhost:8080',
    BASE_PATH = '/PaymentGateway';

app.use(express.json());

app.use(express.urlencoded({ extended: true }));

app.use(cors({ credentials: true, origin: ORIGIN_URL }));

app.listen(port, () => console.log('Payment Gateway live on ' + port));

app.get(BASE_PATH + '/', (req, res) => {
    res.header('Access-Control-Allow-Origin', ORIGIN_URL);
    res.send({ message: 'Payment Gateway is running...' });
});


// Credit Card Charge
router.post(BASE_PATH + '/creditcard/charge', (req, res) => {

    let cardNumber = req.body.cardNumber;
    let amount = req.body.amount;

    res.header('Access-Control-Allow-Origin', ORIGIN_URL);
    res.set('Content-Type', 'application/json');

    let response = chargeAccountResponse(cardNumber, amount)

    res.send(JSON.stringify(response));
    res.end();

    let method = req.method;
    let url = req.url;
    let status = res.statusCode;
    let logInformation = method + ':' + url + ' ' + status;

    logAPIInformation(logInformation, response);
});

// Credit Card Credit
router.post(BASE_PATH + '/creditcard/credit', (req, res) => {

    let cardNumber = req.body.cardNumber;
    let amount = req.body.amount;

    res.header('Access-Control-Allow-Origin', ORIGIN_URL);
    res.set('Content-Type', 'application/json');

    let response = creditAccountResponse(cardNumber, amount)

    res.send(JSON.stringify(response));
    res.end();

    let method = req.method;
    let url = req.url;
    let status = res.statusCode;
    let logInformation = method + ':' + url + ' ' + status;

    logAPIInformation(logInformation, response);
});


function chargeAccountResponse(cardNumber, amount) {

    let zero = 0;
    
    switch (cardNumber) {
        case '4444333322221111':
            return {
                paymentStatus: 'Failed',
                paymentStatusReason: 'Card has been declined',
                amountCharged: zero.toFixed(2),
                cardEnding: cardNumber.slice(-4)
            }
        case '5555444433331111':
            return {
                paymentStatus: 'Failed',
                paymentStatusReason: 'Fraud detected',
                amountCharged: zero.toFixed(2),
                cardEnding: cardNumber.slice(-4)
            }
        default:
            return {
                paymentStatus: 'Success',
                amountCharged: amount.toFixed(2),
                cardEnding: cardNumber.slice(-4)
            }
        }
}

function creditAccountResponse(cardNumber, amount) {
    
    let zero = 0;

    switch (cardNumber) {
        case '4444333322221111':
            return {
                paymentStatus: 'Failed',
                paymentStatusReason: 'Card has been declined',
                amountCredited: zero.toFixed(2),
                cardEnding: cardNumber.slice(-4)
            }
        case '5555444433331111':
            return {
                paymentStatus: 'Failed',
                paymentStatusReason: 'Fraud detected',
                amountCredited: zero.toFixed(2),
                cardEnding: cardNumber.slice(-4)
            }
        default:
            return {
                paymentStatus: 'Success',
                amountCredited: amount.toFixed(2),
                cardEnding: cardNumber.slice(-4)
            }
        }
}

function logAPIInformation(logInformation, mockedResponse) {

    // Log request and response information
    console.log('===============================================================Start API call');
    console.log(logInformation);
    console.log('Response:');
    console.log(JSON.stringify(mockedResponse));
    console.log('===============================================================End API call');
}

// add router in the Express app.
app.use('/', router);


