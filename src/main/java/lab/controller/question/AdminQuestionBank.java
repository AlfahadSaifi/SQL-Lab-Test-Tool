package lab.controller.question;
// author - Sarthak & Ashish

import lab.dto.questionBank.QuestionDto;
import lab.dto.schema.SchemaDTO;
import lab.entity.questionBank.DeleteStatus;
import lab.entity.questionBank.QuestionDifficulty;
import lab.service.questionBank.QuestionBankService;
import lab.service.schema.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminQuestionBank {
    @Autowired
    private QuestionBankService questionBankService;


    @Autowired
    private SchemaService schemaService;


    @GetMapping("viewQuestionBank")
    public ModelAndView addingfromQB(RedirectAttributes redirectAttributes){
        ModelAndView mv = new ModelAndView();
        List<QuestionDto> questions = questionBankService.fetch();

        if(questions.isEmpty())
        {
            redirectAttributes.addFlashAttribute("errorMessage","Question bank is empty"); //if question bank is empty
            mv.setViewName("redirect:/admin/dashboard");
        }
        else {
            mv.addObject("questionList", questions);
            mv.setViewName("pages/question/viewQuestionBank");
        }
        return mv;
    }

    @GetMapping("addInQuestionBank")
    public ModelAndView addingInQB(@ModelAttribute("question") QuestionDto questionDto, RedirectAttributes redirectAttributes){
        ModelAndView mv = new ModelAndView("pages/question/addInQuestionBank");
        List<SchemaDTO> schemas = schemaService.fetchAllSchema();
        mv.addObject("schemaList",schemas);
        return mv;
    }


//    @PostMapping("/addInQuestionBankViaForm")
//    public String addQuestionViaFormInLab(@ModelAttribute("question") QuestionDto questionDto,
//                                          RedirectAttributes redirectAttributes,
//                                          @RequestParam("difficulty") String difficulty,
//                                          @RequestParam("schemaType") String schemaType,
//                                          @RequestParam("existingSchemaId") int schemaReferenceId,
//                                          @RequestParam("newSchemaName") String newSchemaName,
//                                          @RequestParam("referenceFile") MultipartFile referenceFile) {
//
////            questionDto.setQuestionDifficulty(QuestionDifficulty.valueOf(difficulty));
//            SchemaDTO schemaDTO = new SchemaDTO();
//
//
//            if("existing".equals(schemaType))
//            {
//                 schemaDTO = schemaService.fetchById(schemaReferenceId);
//            }
//            else if ("new".equals(schemaType)) {
//                schemaDTO.setSchemaName(newSchemaName);
//                try {
//                    schemaDTO.setReferenceFile(referenceFile.getBytes());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            else { //for generic
//                 schemaDTO = null;
//            }
//            questionDto.setSchema(schemaDTO);
//
//        try {
////            questionBankService.insertQuestion(questionDto);
//            System.out.println("question -->"+questionDto);
//            redirectAttributes.addFlashAttribute("successMessage", "Question added successful");
//        } catch (Exception ex) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Question added fail");
//        }
//        return "redirect:/admin/viewQuestionBank";
//    }

    @PostMapping("/addInQuestionBankViaForm")
    public String addQuestionViaFormInLab(@ModelAttribute("question") QuestionDto questionDto,
                                          RedirectAttributes redirectAttributes,
                                          @RequestParam("difficulty") String difficulty,
                                          @RequestParam("schemaType") String schemaType,
                                          @RequestParam(name = "existingSchemaId", required = false) Integer schemaReferenceId,
                                          @RequestParam(name = "newSchemaName", required = false) String newSchemaName,
                                          @RequestParam(name = "referenceFile", required = false) MultipartFile referenceFile
    ) {

        SchemaDTO schemaDTO = null;
        questionDto.setQuestionDifficulty(QuestionDifficulty.valueOf(difficulty));

        if ("existing".equals(schemaType)) {
            if (schemaReferenceId != null) {
                schemaDTO = schemaService.fetchById(schemaReferenceId);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to fetch the selected schema");
            }
        }

        else if ("new".equals(schemaType)) {
            if (newSchemaName != null && referenceFile != null && !referenceFile.isEmpty()) {
                schemaDTO = new SchemaDTO();
                schemaDTO.setSchemaName(newSchemaName); // setting name

                try {
                    schemaDTO.setReferenceFile(referenceFile.getBytes()); //setting reference
                    int referenceId = schemaService.insertSchema(schemaDTO);  //inserting schema and getting Auto Generated Id to set in DTO (for mapping)
                    schemaDTO.setSchemaReferenceId(referenceId); // DTO has auto generated ID corresponding to DB
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to add schema");
            }
        }

        questionDto.setSchema(schemaDTO);
        questionDto.setDeleteStatus(DeleteStatus.FALSE);

        try {
            // Process questionDto and save it
             if(questionBankService.insertQuestion(questionDto)) {

                 redirectAttributes.addFlashAttribute("successMessage", "Question added successfully");
             }
             else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to add question");
             }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add question");
        }

        return "redirect:/admin/viewQuestionBank";
    }



    @GetMapping("/editQuestionInQB")
    public String editQuestion(@RequestParam("questionId") int questionId, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            QuestionDto questionDto = questionBankService.fetchById(questionId);
            model.addAttribute("question", questionDto);
            redirectAttributes.addFlashAttribute("successMessage", "Question edit successful");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Question edit fail");
        }
        return "pages/question/editQuestionInQB";
    }

    @RequestMapping("/editQuestionInQBViaForm")
    public String editQuestionViaForm(@ModelAttribute("question") QuestionDto questionDto, @RequestParam("questionId") int questionId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            questionDto.setQuestionId(questionId);
            System.out.println(questionDto);
            boolean res = questionBankService.editQuestion(questionDto);
            System.out.println(res);
            redirectAttributes.addFlashAttribute("successMessage", "Question edit successful");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Question edit fail");
        }
        return "redirect:/admin/viewQuestionBank";
    }

    @RequestMapping("/deleteQuestionInQB")
    public String deleteQuestionViaForm(@RequestParam("questionId") int questionId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            questionBankService.changeDeleteStatus(questionId); // soft delete changes the delete status to true
            redirectAttributes.addFlashAttribute("successMessage", "Question delete successful");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
        }
        return "redirect:/admin/viewQuestionBank";
    }
}
