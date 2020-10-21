var gl;
var canvas;
var shaderProgram;

// simplify life - no namespace conflicts here
mat4 = glMatrix.mat4;
vec3 = glMatrix.vec3;
var originView = true;
var extView = false;
var cameraPosition = [0,0,0];

var lightPosition = vec3.fromValues(0, 0, 0);
var lightAmbient = vec3.fromValues(0.1, 0.1, 0.1);
var lightDiffuse = vec3.fromValues(300, 300, 300);

var materialAmbient = vec3.fromValues(1, 1, 1);
var materialDiffuse = vec3.fromValues(1, 1, 1);

window.onload = function init() {
    // Get A WebGL context
    canvas = document.getElementById( "canvas" );
    gl = WebGLUtils.setupWebGL( canvas );
    if ( !gl ) 
        alert( "WebGL isn't available" );

    // setup GLSL program
    shaderProgram = initShaders( gl, "vertex-shader", "fragment-shader" );
    gl.useProgram(shaderProgram);

    defineAttributes();
    initBuffers();
    initTextures();

    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.enable(gl.DEPTH_TEST);


    var ambientProduct = vec3.create();
    vec3.multiply(ambientProduct, lightAmbient, materialAmbient);
    var diffuseProduct = vec3.create();
    vec3.multiply(diffuseProduct, lightDiffuse, materialDiffuse);
        
    gl.uniform3fv(gl.getUniformLocation(shaderProgram, "ambientProduct"), ambientProduct);
    gl.uniform3fv(gl.getUniformLocation(shaderProgram, "diffuseProduct"), diffuseProduct);
    gl.uniform3fv(gl.getUniformLocation(shaderProgram, "lightPosition"), lightPosition);


    render();

    // to trigger where to view it from
    document.getElementById("originView").onchange = function(event) { 
        originView = event.target.checked; 
        extView = false;
        render();
    };
    document.getElementById("extView").onchange = function(event) { 
        extView = event.target.checked; 
        originView = false;
        render();
    };
}

function defineAttributes() {
    // enable shader attributes and map them to local properties of shaderProgram
    shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
    gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);

    shaderProgram.textureCoordAttribute = gl.getAttribLocation(shaderProgram, "aTextureCoord");
    gl.enableVertexAttribArray(shaderProgram.textureCoordAttribute);
    
    shaderProgram.vertexNormalAttribute = gl.getAttribLocation(shaderProgram, "aVertexNormal");
    gl.enableVertexAttribArray(shaderProgram.vertexNormalAttribute);
    

    shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
    shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
    shaderProgram.viewMatrixUniform = gl.getUniformLocation(shaderProgram, "uViewMatrix");
    shaderProgram.viewPos = gl.getUniformLocation(shaderProgram, "viewPos");

    shaderProgram.samplerUniform = gl.getUniformLocation(shaderProgram, "uSampler");

}

var moonTexture;
var earthTexture;

function initTextures() {
    // we cannot bind the images to textures until they have fully loaded
    moonTexture = gl.createTexture();
    moonTexture.image = new Image();
    moonTexture.image.onload = function () {
        bindTextures(moonTexture)
    }
    moonTexture.image.src = "moon.gif";

    earthTexture = gl.createTexture();
    earthTexture.image = new Image();
    earthTexture.image.onload = function () {
        bindTextures(earthTexture)
    }
    earthTexture.image.src = "earth.jpg";
}

function bindTextures(texture) {
    gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
    gl.bindTexture(gl.TEXTURE_2D, texture);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_NEAREST);
    gl.generateMipmap(gl.TEXTURE_2D);

    gl.bindTexture(gl.TEXTURE_2D, null);
}

// Model-View Matrix
var mvMatrix = mat4.create();
var mvMatrixStack = [];
// Projection Matrix
var pMatrix = mat4.create();
// View Matrix
var viewMatrix = mat4.create();

function mvPushMatrix() {
    var copy = mat4.create();
    mat4.copy(copy, mvMatrix);
    mvMatrixStack.push(copy);
}

function mvPopMatrix() {
    if (mvMatrixStack.length == 0) {
        throw "Invalid popMatrix!";
    }
    mvMatrix = mvMatrixStack.pop();
}

function setMatrixUniforms() {
    gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
    gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
    gl.uniformMatrix4fv(shaderProgram.viewMatrixUniform, false, viewMatrix);

    gl.uniform3fv(gl.getUniformLocation(shaderProgram, "viewPos"), cameraPosition);
}

function degToRad(degrees) {
    return degrees * Math.PI / 180;
}

var earthVertexPositionBuffer;
var earthVertexNormalBuffer;
var earthVertexTextureCoordBuffer;
var earthVertexIndexBuffer;

var moonVertexPositionBuffer;
var moonVertexNormalBuffer;
var moonVertexTextureCoordBuffer;
var moonVertexIndexBuffer;

// create sphere (vertex coordinates & normal vectors) with mapping to texture coordinates
// also initialize triangle index data
function createSphere(latitude, longitude, radius)
{
    var sphere = {};
    sphere.vertexPositionData = [];
    sphere.normalData = [];
    sphere.textureCoordData = [];
    sphere.indexData = [];

    // vertex data
    for ( var latNumber = 0; latNumber <= latitude; latNumber++ ) {
        var theta = latNumber * Math.PI / latitude;
        var sinTheta = Math.sin(theta);
        var cosTheta = Math.cos(theta);
        for ( var longNumber = 0; longNumber <= longitude; longNumber++ ) {
            var phi = longNumber * 2 * Math.PI / longitude;
            var sinPhi = Math.sin(phi);
            var cosPhi = Math.cos(phi);

            var x = cosPhi * sinTheta;
            var y = cosTheta;
            var z = sinPhi * sinTheta;
            var u = 1 - (longNumber / longitude);
            var v = 1 - (latNumber / latitude);

            sphere.normalData.push(x);
            sphere.normalData.push(y);
            sphere.normalData.push(z);
            sphere.textureCoordData.push(u);
            sphere.textureCoordData.push(v);
            sphere.vertexPositionData.push(radius * x);
            sphere.vertexPositionData.push(radius * y);
            sphere.vertexPositionData.push(radius * z);
        }
    }

    // triangles
    for ( var latNumber=0; latNumber < latitude; latNumber++ ) {
        for ( var longNumber=0; longNumber < longitude; longNumber++ ) {
            var first = (latNumber * (longitude + 1)) + longNumber;
            var second = first + longitude + 1;

            sphere.indexData.push(first);
            sphere.indexData.push(second);
            sphere.indexData.push(first + 1);

            sphere.indexData.push(second);
            sphere.indexData.push(second + 1);
            sphere.indexData.push(first + 1);
        }
    }

    return sphere;
}

// initialize data buffers to hold eart & moon data in triangle format
function initBuffers() {

    var earth = createSphere(30, 30, 4);

    earthVertexNormalBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, earthVertexNormalBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(earth.normalData), gl.STATIC_DRAW);
    earthVertexNormalBuffer.itemSize = 3;
    earthVertexNormalBuffer.numItems = earth.normalData.length / 3;

    earthVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, earthVertexTextureCoordBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(earth.textureCoordData), gl.STATIC_DRAW);
    earthVertexTextureCoordBuffer.itemSize = 2;
    earthVertexTextureCoordBuffer.numItems = earth.textureCoordData.length / 2;

    earthVertexPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, earthVertexPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(earth.vertexPositionData), gl.STATIC_DRAW);
    earthVertexPositionBuffer.itemSize = 3;
    earthVertexPositionBuffer.numItems = earth.vertexPositionData.length / 3;

    earthVertexIndexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, earthVertexIndexBuffer);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(earth.indexData), gl.STREAM_DRAW);
    earthVertexIndexBuffer.itemSize = 1;
    earthVertexIndexBuffer.numItems = earth.indexData.length;

    var moon = createSphere(30, 30, 1);

    moonVertexNormalBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, moonVertexNormalBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(moon.normalData), gl.STATIC_DRAW);
    moonVertexNormalBuffer.itemSize = 3;
    moonVertexNormalBuffer.numItems = moon.normalData.length / 3;

    moonVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, moonVertexTextureCoordBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(moon.textureCoordData), gl.STATIC_DRAW);
    moonVertexTextureCoordBuffer.itemSize = 2;
    moonVertexTextureCoordBuffer.numItems = moon.textureCoordData.length / 2;

    moonVertexPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, moonVertexPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(moon.vertexPositionData), gl.STATIC_DRAW);
    moonVertexPositionBuffer.itemSize = 3;
    moonVertexPositionBuffer.numItems = moon.vertexPositionData.length / 3;

    moonVertexIndexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, moonVertexIndexBuffer);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(moon.indexData), gl.STREAM_DRAW);
    moonVertexIndexBuffer.itemSize = 1;
    moonVertexIndexBuffer.numItems = moon.indexData.length;
}

var moonAngle = 180;
var earthAngle = 0;
var earthOrbitAngle = 180;
var earthPos = [0,0,-50];
var moonOrbitAngle = 180;
var moonPos = [40,0,0];


function drawScene() {
    gl.viewport(0, 0, canvas.clientWidth, canvas.clientHeight);
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);


    mat4.perspective(pMatrix, 45, canvas.clientWidth / canvas.clientHeight, 0.1, 200.0);
    mat4.identity(mvMatrix);

    if (extView){
        cameraPosition = [0,50,20];
        mat4.lookAt(viewMatrix, cameraPosition, [0,0,0], [0,1,0]); // changing the camera view to look at it externally from above
    } else {
        cameraPosition = [0,10,40];
        mat4.lookAt(viewMatrix, cameraPosition, [0,0,0], [0,1,0]); // changing the camera view to look at it externally from above
    }
    // draw the moon
    gl.uniform3fv(gl.getUniformLocation(shaderProgram, "shadowObjPos"), earthPos);
    gl.uniform1f(gl.getUniformLocation(shaderProgram, "shadowObjRad"), 4);
    // draw the moon
    mvPushMatrix();
    // specify position and angle
    mat4.translate(mvMatrix, mvMatrix, moonPos) // tranlating based on moon postion in orbit around earth
    mat4.rotate(mvMatrix, mvMatrix, degToRad(moonAngle), [0, 1, 0]);

    gl.activeTexture(gl.TEXTURE0);
    gl.bindTexture(gl.TEXTURE_2D, moonTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, moonVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, moonVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, moonVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, moonVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, moonVertexNormalBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexNormalAttribute, moonVertexNormalBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, moonVertexIndexBuffer);
    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, moonVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);
    mvPopMatrix();
    // draw the earth
    gl.uniform3fv(gl.getUniformLocation(shaderProgram, "shadowObjPos"), moonPos);
    gl.uniform1f(gl.getUniformLocation(shaderProgram, "shadowObjRad"), 1);
    mvPushMatrix();
    // specify position and angle
    mat4.translate(mvMatrix, mvMatrix, earthPos) // translating based on earth postion in orbit
    mat4.rotate(mvMatrix, mvMatrix, degToRad(earthAngle), [0, 1, 0]);

    gl.bindBuffer(gl.ARRAY_BUFFER, earthVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, earthVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
    
    gl.bindBuffer(gl.ARRAY_BUFFER, earthVertexNormalBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexNormalAttribute, earthVertexNormalBuffer.itemSize, gl.FLOAT, false, 0, 0);
    
    gl.bindBuffer(gl.ARRAY_BUFFER, earthVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, earthVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.activeTexture(gl.TEXTURE0);
    gl.bindTexture(gl.TEXTURE_2D, earthTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 0);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, earthVertexIndexBuffer);
    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, earthVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);
    mvPopMatrix();




    

}

var lastTime = 0;

function render() {
    var timeNow = new Date().getTime();
    drawScene();
    if (lastTime != 0) {
        var elapsed = timeNow - lastTime;

        moonAngle += 0.05 * elapsed;
        earthAngle += 0.05 * elapsed;
        // calculating the orbit angle depending on the time elapsed
        earthOrbitAngle += 0.001 * elapsed; 
        moonOrbitAngle += 0.005 * elapsed;

        // calculating the X and Z postions based on the angle
        earthPos[0] = 20 * Math.cos(earthOrbitAngle);
        earthPos[2] = 20 * Math.sin(earthOrbitAngle);
        var mX = 7 * Math.cos(moonOrbitAngle)
        var mZ = 7 * Math.sin(moonOrbitAngle)
        // the moon orbits the earth, so the X and Z positions are based on earth's X and Z positions.
        moonPos[0] = mX  + earthPos[0]; 
        moonPos[2] = mZ + earthPos[2];
    }
    lastTime = timeNow;
    requestAnimFrame(render)
}

