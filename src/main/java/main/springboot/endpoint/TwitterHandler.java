package main.springboot.endpoint;

import org.springframework.web.bind.annotation.*;

@RestController
public class TwitterHandler {
    @SuppressWarnings("unused")
    @RequestMapping(value = "webhook/dbl", consumes = "application/json", produces = "application/json",
            method = RequestMethod.POST)
    public void onFollow(@RequestHeader(value = "Authorization") String token, @RequestBody String payload){

    }
}
