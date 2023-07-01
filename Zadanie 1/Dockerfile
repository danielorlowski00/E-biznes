FROM ubuntu:22.04

ENV JAVA_VERSION=8.0.372-zulu
ENV KOTLIN_VERSION=1.8.20
ENV PYTHON_VERSION=python3.8
ENV TZ=Europe/Warsaw

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get update && apt-get install -y zip unzip curl
RUN apt install -y software-properties-common
RUN add-apt-repository -y ppa:deadsnakes/ppa
RUN apt install -y ${PYTHON_VERSION} python3-pip
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.10 1 && \
    update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.8 2
RUN curl -s "https://get.sdkman.io" | bash && \
    /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    yes | sdk install java ${JAVA_VERSION} && \
    yes | sdk install kotlin ${KOTLIN_VERSION} && \
    rm -rf $HOME/.sdkman/archives/* && \
    rm -rf $HOME/.sdkman/tmp/*"