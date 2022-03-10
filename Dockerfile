# Dockerfile from
# https://github.com/docToolchain/docker-image/blob/ng-beta/alpine/Dockerfile
FROM openjdk:14-jdk-alpine

# see https://github.com/docker-library/openjdk/issues/73
ENV LC_CTYPE en_US.UTF-8

RUN addgroup -S dtcgroup && adduser -S dtcuser -G dtcgroup

RUN apk update && apk add --no-cache build-base

RUN	echo "add needed tools" && \
    apk add --no-cache curl wget zip unzip git bash --virtual build-dependencies build-base\
    git \
    graphviz \
    python \
    ruby-dev \
    py-pygments \
    libc6-compat \
    ttf-dejavu 
RUN gem update --system  --no-rdoc --no-ri
RUN gem install rdoc --no-document 
RUN gem install pygments.rb

# Add pandoc
# Reinstall any system packages required for runtime of pandoc.
RUN apk --no-cache add \
        gmp \
        libffi \
        lua5.3 \
        lua5.3-lpeg
COPY --from=pandoc/core:2.9 \
    /usr/bin/pandoc \
    /usr/bin/pandoc-citeproc \
    /usr/bin/

# Add pandoc
# https://github.com/advancedtelematic/dockerfiles/blob/master/doctools/Dockerfile
#RUN apk add --no-cache cmark --repository http://nl.alpinelinux.org/alpine/edge/testing && \
#    apk add --no-cache --allow-untrusted pandoc --repository https://conoria.gitlab.io/alpine-pandoc/

SHELL ["/bin/bash", "-c"]

USER dtcuser
WORKDIR /home/dtcuser
ENV HOME=/home/dtcuser

ENV GRADLE_USER_HOME=/home/dtcuser/.gradle


COPY --chown=dtcuser:dtcuser . /home/dtcuser/docToolchain
# RUN     git clone --branch ng-shadoc https://github.com/Judikael/docToolchain.git

RUN     cd docToolchain && \
        # git fetch --tags && \
        # git checkout v2.0.4 && \
        git submodule update -i && \
        # remove .git folders
        rm -rf `find -type d -name .git` && \
        umask g+w && \
        # Preload dependencies in order to execute without download from internet
        ./gradlew --write-verification-metadata sha256 help && \
        rm -f gradle/verification-metadata.xml && \
        ./gradlew tasks && \
        ./gradlew dependencies && \
        ./gradlew generateHTML generatePDF && \        
        chmod -R o=u $GRADLE_USER_HOME && \
        chmod -R g=u $GRADLE_USER_HOME && \
        rm -r $GRADLE_USER_HOME/daemon && \
        chmod -R o=u $HOME    

ENV PATH="/home/dtcuser/docToolchain/bin:${PATH}"

USER dtcuser

WORKDIR /project

VOLUME /project

ENTRYPOINT ["doctoolchain"]
CMD [".", "--help"]
