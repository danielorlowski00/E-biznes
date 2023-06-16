from flask import Flask, request
import openai

app = Flask(__name__)

openai.api_key = 'YOUR API TOKEN HERE'


@app.route('/chat', methods=['POST'])
def chat():
    data = request.get_json()
    message = data['message']
    response = openai.Completion.create(
        engine='text-davinci-003',
        prompt=message,
        max_tokens=1000
    )
    return response.choices[0]['text']


if __name__ == '__main__':
    app.run()
