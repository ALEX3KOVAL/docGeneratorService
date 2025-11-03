FROM telemark/docker-node-unoconv:10.14.0

RUN git clone https://github.com/zrrrzzt/tfk-api-unoconv.git unoconvservice

WORKDIR /unoconvservice

RUN npm install --production

ARG SERVER_PORT=3000

ENV SERVER_PORT $SERVER_PORT
ENV PAYLOAD_MAX_SIZE 8048576
ENV TIMEOUT_SERVER 120000
ENV TIMEOUT_SOCKET 140000

EXPOSE 8734

ENTRYPOINT /usr/bin/unoconv --listener --server=0.0.0.0 --port=2002 & node standalone.js
