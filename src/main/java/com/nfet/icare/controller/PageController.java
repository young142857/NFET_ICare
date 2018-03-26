package com.nfet.icare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
	@RequestMapping(value = "/page/{name}", method = RequestMethod.GET)
	public String pageRedirect(@PathVariable String name) {
		return name;
	}
}
