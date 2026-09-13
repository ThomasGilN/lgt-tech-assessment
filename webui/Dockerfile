FROM node:24-alpine AS build

WORKDIR /workspace

COPY package.json package-lock.json ./
RUN sed -i 's#https://repository.eng.netsuite.com/3/repository/npm-stable/#https://registry.npmjs.org/#g' package-lock.json \
    && npm ci

COPY . .

ARG VITE_KEYCLOAK_URL=http://localhost:8080/auth
ENV VITE_KEYCLOAK_URL=${VITE_KEYCLOAK_URL}

RUN npm run build

FROM nginx:1.29-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist /usr/share/nginx/html

EXPOSE 80
