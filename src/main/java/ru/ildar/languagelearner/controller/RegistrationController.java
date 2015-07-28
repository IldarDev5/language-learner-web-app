package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ildar.languagelearner.controller.dto.UserDTO;
import ru.ildar.languagelearner.exception.EmailAlreadyExistsException;
import ru.ildar.languagelearner.exception.NicknameAlreadyExistsException;
import ru.ildar.languagelearner.service.AppUserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth/registration")
public class RegistrationController
{
    @Autowired
    private AppUserService appUserService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registrationPage(UserDTO userDTO)
    {
        return new ModelAndView("auth/registration", "user", userDTO);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute("user") @Valid UserDTO userDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes)
    {
        if(bindingResult.hasFieldErrors())
        {
            return registrationPage(userDTO);
        }

        if(!userDTO.getPassword().equals(userDTO.getRepeatPassword()))
        {
            bindingResult.rejectValue("password", null, "Passwords do not match.");
            return registrationPage(userDTO);
        }

        try
        {
            appUserService.addNewUser(userDTO);
        }
        catch (NicknameAlreadyExistsException e)
        {
            bindingResult.rejectValue("nickname", null, "User with such nickname is already present.");
            return registrationPage(userDTO);
        }
        catch (EmailAlreadyExistsException e)
        {
            bindingResult.rejectValue("email", null, "User with such E-Mail is already present.");
            return registrationPage(userDTO);
        }

        redirectAttributes.addAttribute("isRegistered", true);
        return new ModelAndView("redirect:/auth/login");
    }
}
