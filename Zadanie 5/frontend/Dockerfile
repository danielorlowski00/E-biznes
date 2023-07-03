FROM node:18

ENV REACT_APP_BACKEND_URL=https://e-biznes-95e2421c913d.herokuapp.com/

WORKDIR /app

COPY ./package.json /app
RUN npm install
COPY . .
CMD ["npm", "start"]
