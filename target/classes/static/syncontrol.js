        var meAdmin = 'null';
        var imma_here = false;
        var csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');


        function getAdmin(){
            var xhr = new XMLHttpRequest();
            //xhr.open("GET",'https://' + window.location.host + '/videos/meAdmin', false);
            xhr.open("GET",'http://' + window.location.host + '/videos/meAdmin', false);
            xhr.onload = function(){meAdmin =  xhr.responseText;};
            xhr.send();
        }
        function onMeAdmin(){
            if (meAdmin == 'true'){
                myVideo = videojs('video1');
                myVideo.on('pause', function() {
                    var xhr = new XMLHttpRequest();
                    //xhr.open("POST",'https://' + window.location.host + '/videos/pause/' + myVideo.currentTime(), false);
                    xhr.open("POST",'http://' + window.location.host + '/sse/pause/' + myVideo.currentTime(), false);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send('_csrf=' + encodeURIComponent(csrfToken));
                });

                myVideo.on('seeking', function() {
                    var xhr = new XMLHttpRequest();
                    //xhr.open("POST",'https://' + window.location.host + '/videos/seek/' + myVideo.currentTime(), false);
                    xhr.open("POST",'http://' + window.location.host + '/sse/seek/' + myVideo.currentTime(), false);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send('_csrf=' + encodeURIComponent(csrfToken));
                });

                myVideo.on('seeked', function() {
                    var xhr = new XMLHttpRequest();
                    //xhr.open("POST",'https://' + window.location.host + '/videos/seek/' + myVideo.currentTime(), false);
                    xhr.open("POST",'http://' + window.location.host + '/sse/seek/' + myVideo.currentTime(), false);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send('_csrf=' + encodeURIComponent(csrfToken));
                });

                myVideo.on('play', function() {
                    var xhr = new XMLHttpRequest();
                    //xhr.open("POST",'https://' + window.location.host + '/videos/play/' + myVideo.currentTime(), false);
                    xhr.open("POST",'http://' + window.location.host + '/sse/play/' + myVideo.currentTime(), false);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send('_csrf=' + encodeURIComponent(csrfToken));
                });

                adminNotifyerInit();
            }
        };

        function onMeClient(){
            if (meAdmin == 'false'){

                sseNotyfierInit();

                myVideo = videojs('video1');
                myVideo.on('play', function() {
                    imma_here = true;
                    var xhr = new XMLHttpRequest();
                    //xhr.open("POST",'https://' + window.location.host + '/videos/immahere', false);
                    xhr.open("POST",'http://' + window.location.host + '/sse/immahere', false);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send('_csrf=' + encodeURIComponent(csrfToken));
                });
            }
        }

        function getRoomFile(){
            var xhr = new XMLHttpRequest();
            //xhr.open("GET",'https://' + window.location.host + '/videos/some', false);
            xhr.open("GET",'http://' + window.location.host + '/videos/some', false);
            xhr.onload = function() {
                myVideo = videojs('video1');
                console.log(xhr);
                //myVideo.src('https://' + window.location.host + '/' + xhr.responseText);
                myVideo.src('http://' + window.location.host + '/videos/' + xhr.responseText);
                videojs('video1');
                myVideo.ready();
            }
            xhr.onerror = function(){
                window.alert("ERROR")
            }
            xhr.send();
        }

        function sseNotyfierInit(){
            var nSse = new EventSource('/sse/action_notify');
            console.log(nSse);
            var myVideo = videojs('video1');
            nSse.onopen = function (){
                //window.alert("OPENED");
            }

            nSse.onerror = function (){
                //window.alert("ERROR");
                nSse.close()
            }
            nSse.addEventListener('paused', function(event) {
                myVideo.pause();
                myVideo.currentTime(event.data);
            });

            nSse.addEventListener('played', function(event) {
                myVideo.currentTime(event.data);
                myVideo.play();
            });
            nSse.addEventListener('seeked', function(event) {
                if(meAdmin == 'false'){
                    myVideo.currentTime(event.data);
                }
            });
            nSse.addEventListener('ctime', function(event){
                if (imma_here === true){
                    imma_here = false;
                    var myVideo = videojs('video1');
                    myVideo.currentTime(event.data);
                }
            });
        }

        function adminNotifyerInit(){
            var nSse = new EventSource('/sse/admin_notify');
            //var xhr = new XMLHttpRequest();
            nSse.addEventListener('user_here', function(event){
                    var xhr = new XMLHttpRequest();
                    //xhr.open("POST",'https://' + window.location.host + '/videos/stime/' + myVideo.currentTime(), false);
                    xhr.open("POST",'https://' + window.location.host + '/sse/stime/' + myVideo.currentTime(), false);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.send('_csrf=' + encodeURIComponent(csrfToken));
                }
            );

        }
        getAdmin();
        onMeClient();
        onMeAdmin();
        getRoomFile();

