package com.example.BrancoGarcia_Tingeso_Evaluacion3.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.*;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.InfoRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.ScoresRepository;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoresService {
    @Autowired
    ScoresRepository scoresRepository;

    @Autowired
    PrerequisitesService prerequisitesService;
    @Autowired
    StudentService studentService;
    @Autowired
    SubjectsService subjectsService;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    CareerService careerService;

    @Autowired
    InfoService infoService;


    // Para agregar los bloques de un ramo
    public Integer addSchedulesToSubject(long id_subject, List<Integer> schedules){
        // Primero vemos los estudiantes inscritos a ese ramo y los desinscribimos
        List<ScoresEntity> list_scores = enrolled_students(id_subject);
        for(ScoresEntity scores : list_scores){
            // Desinscribo cada estudiante previamente inscrito en el ramo
            String rut = scores.getRut();
            unsubscribe(rut, id_subject);
        }
        // Modifico los horarios del ramo
        Integer result = subjectsService.addManySchedules(id_subject, schedules);
        // Luego vuelvo a inscribir a los estudiantes (a algunos puede causarles tope)
        // Solo si se modificaron correctamente los horarios del ramo
        if(result == 1){
            for(ScoresEntity scores : list_scores){
                // Los voy inscribiendo nuevamente
                String rut = scores.getRut();
                enroll_subject(rut, id_subject);
            }
        }
        return result; // exito


    }

    // Número de estudiantes inscritos de todos los ramos
    public List<Object[]> count_inscriptions() {
        return scoresRepository.count_inscriptions();
    }

    // Para obtener los estudiantes inscritos de un ramo
    public List<ScoresEntity> enrolled_students(long id_subject) {
        return scoresRepository.enrolled_students(id_subject);
    }

    // Para ver historial de notas de un estudiante
    public List<ScoresEntity> score_history_student(String rut){
        return scoresRepository.score_history_student(rut);
    }

    // Para ver las notas que tuvo un estudiante en un ramo
    public List<ScoresEntity> history_scores(String rut, long id_subject){
        return scoresRepository.history_score(rut, id_subject);
    }

    // Guardar lista de ramos de una persona en la base de datos
    public List<InfoEntity> saveSubjectsStudentInfo(String rut){
        // Si ya se ha guardado previamente la información, no se vuelve a guardar
        if(infoService.getInfoByRut(rut) == null || infoService.getInfoByRut(rut).size() < 1){
            Optional<StudentEntity> s = studentService.getStudentByRut(rut);
            // Estados: -2 -> ambos, -1 -> reprobó 3 veces, 0 -> cursó al menos 3 asignaturas
            int canRegister = 0;
            if(s.isPresent()){
                // Obtengo los ramos de la carrera de esa persona
                List<SubjectsEntity> subjects = subjectsService.getSubjectsByCareerId(s.get().getId_career());
                for(SubjectsEntity sub : subjects){ // Por cada ramo creo una fila en Info
                    InfoEntity info = new InfoEntity();
                    info.setRut(rut);
                    info.setId_subject(sub.getId_subject());
                    info.setName_subject(sub.getName());
                    info.setLevel(sub.getLevel());
                    // Veo si se aprobó el ramo
                    if(isApproved(rut, sub.getId_subject()) >= 1){
                        // Significa que está aprobado
                        info.setState(1);
                    }
                    // Si no se ha aprobado el ramo
                    else{
                        // Falta por aprobar
                        info.setState(0);
                        // Hay que ver si ha sido registrado
                        if(scoresRepository.isInscribed(rut, sub.getId_subject()) >= 1){
                            // Si ya se registró
                            info.setCanRegister(1);
                            info.setCapacity(sub.getCapacity());
                            info.setStudents_inscribed(sub.getInscribed());
                            info.setSchedule(sub.getS_schedule());
                            info.setIs_inscribed(1); // está inscrito
                        }
                        else{ // En caso de no estar inscrito en el ramo
                            // Veo si se aprobaron sus requisitos
                            canRegister = approved_prerequisites(rut, sub.getId_subject());
                            // Si se puede inscribir el ramo se muestra su capacidad e inscritos
                            if(canRegister == 1){
                                info.setCanRegister(1);
                                info.setCapacity(sub.getCapacity());
                                info.setStudents_inscribed(sub.getInscribed());
                                info.setSchedule(sub.getS_schedule());
                                info.setIs_inscribed(0); // no está inscrito
                            }
                            else{
                                info.setCanRegister(0);
                            }
                        }
                    }
                    infoService.saveInfo(info);
                }
            }
        }
        return infoService.getInfoByRut(rut);
    }


    // Lista de ramos de una persona
    public List<Map<String, Object>> subjectsStudent(String rut){
        List<Map<String, Object>> list_subjects = new ArrayList<>();
        Optional<StudentEntity> s = studentService.getStudentByRut(rut);
        // Estados: -2 -> ambos, -1 -> reprobó 3 veces, 0 -> cursó al menos 3 asignaturas
        int canRegister = 0;
        if(s.isPresent()){
            // Obtengo los ramos de la carrera de esa persona
            List<SubjectsEntity> subjects = subjectsService.getSubjectsByCareerId(s.get().getId_career());
            for(SubjectsEntity sub : subjects){ // Por cada ramo
                Map<String, Object> subjectInfo = new HashMap<>();
                subjectInfo.put("id_subject", sub.getId_subject());
                subjectInfo.put("name_subject", sub.getName());
                subjectInfo.put("level", sub.getLevel());
                // Veo si se aprobó el ramo
                if(isApproved(rut, sub.getId_subject()) >= 1){
                    // Significa que está aprobado
                    subjectInfo.put("state", 1);
                }
                // Si no se ha aprobado el ramo
                else{
                    // Falta por aprobar
                    subjectInfo.put("state", 0);
                    // Hay que ver si ha sido registrado
                    if(scoresRepository.isInscribed(rut, sub.getId_subject()) >= 1){
                        // Si ya se registró
                        subjectInfo.put("canRegister", 1);
                        subjectInfo.put("capacity", sub.getCapacity());
                        subjectInfo.put("students_inscribed", sub.getInscribed());
                        subjectInfo.put("schedule", sub.getS_schedule());
                        subjectInfo.put("is_inscribed", 1); // está inscrito
                    }
                    else{ // En caso de no estar inscrito en el ramo
                        // Veo si se aprobaron sus requisitos
                        canRegister = approved_prerequisites(rut, sub.getId_subject());
                        // Si se puede inscribir el ramo se muestra su capacidad e inscritos
                        if(canRegister == 1){
                            subjectInfo.put("canRegister", 1);
                            subjectInfo.put("capacity", sub.getCapacity());
                            subjectInfo.put("students_inscribed", sub.getInscribed());
                            subjectInfo.put("schedule", sub.getS_schedule());
                            subjectInfo.put("is_inscribed", 0); // no está inscrito
                        }
                        else{
                            subjectInfo.put("canRegister", 0);
                        }
                    }
                }
                list_subjects.add(subjectInfo);
            }
        }
        return list_subjects;
    }

    // Para desincribir un ramo
    // 0: error, 1: éxito
    public int unsubscribe(String rut, Long id_subject) {
        // Para desinscribir busco al estudiante por rut, y al ramo
        Optional<StudentEntity> stu = studentService.getStudentByRut(rut);
        Optional<SubjectsEntity> subj = subjectsService.getSubjectsById(id_subject);
        if(stu.isEmpty() || subj.isEmpty()){
            return 0;
        }
        // Modifico tabla información
        InfoEntity inf = infoService.getInfoByRutAndSubject(rut, id_subject);

        if(inf.getIs_inscribed() == 0){ // si no está inscrito, no puede desincribirse
            return 0;
        }

        inf.setIs_inscribed(0); // deja de estar inscrito
        inf.setStudents_inscribed(inf.getStudents_inscribed() - 1);
        infoService.saveInfo(inf);


        StudentEntity student1 = stu.get();
        SubjectsEntity subject1 = subj.get();

        // Modifico los datos del estudiante
        // Disminuyo la cantidad de ramos tomados
        student1.setEnrolled_subjects(student1.getEnrolled_subjects() - 1);
        // Por cada bloque del ramo
        List<Integer> sch = subject1.getS_schedule();
        for(int i = 0; i < sch.size(); i++){
            // Obtengo el bloque del ramo
            int hor = sch.get(i);
            // Limpio de los inscritos el registro del ramo a desincribir
            student1.getInscribed().set(hor, 0);
            student1.getName_inscribed().set(hor, "");
        }
        studentService.saveStudent(student1);

        // Luego lo que debo hacer es borrar las filas
        // asociadas al ramo y rut en la tabla score (del año 2024)
        List<ScoresEntity> scoresEntities = scoresRepository.getScoresAndSubjects2024(rut, id_subject);
        if(scoresEntities != null){
            for(ScoresEntity scores : scoresEntities){
                scoresRepository.deleteById(scores.getId_score_student());
            }
        }

        // Modifico la cantidad de inscritos en la tabla subject
        // Disminuyo en uno
        subject1.setInscribed(subject1.getInscribed() - 1);
        subjectsService.saveSubject(subject1);



        // Modificar tabla inscritos
        return 1;
    }

    // Para inscribir un ramo
    // -3: sin horario, -2: tope, -1: fracaso por sobrecupo, 0: fracaso por nivel, 1: exito
    public int enroll_subject(String rut, Long id_subject){
        // El número máximo de asignaturas que puede inscribir el o la estudiante
        // corresponde al número de asignaturas de su nivel.
        Optional<StudentEntity> student = studentService.getStudentByRut(rut);
        if(student.get().getEnrolled_subjects() >= student.get().getMax_subjects()){
            // Si ya se ha llegado al máximo de ramos inscritos, no se puede
            // inscribir más
            return 0;
        }
        Optional<SubjectsEntity> subject = subjectsService.getSubjectsById(id_subject);
        // Cada asignatura tiene un cupo máximo de estudiantes.
        if(subject.get().getCapacity() <= subject.get().getInscribed()){
            // Si los inscritos igualan a la capacidad
            return -1; // No se pueden inscribir más personas
        }
        if(subject.get().getS_schedule() == null || subject.get().getS_schedule().isEmpty()){
            // Si no tiene horarios no se puede inscribir
            return -3;
        }
        // veo si hay tope de horario
        StudentEntity student1 = student.get();
        List<Integer> insc = student1.getInscribed();
        List<String> name_insc = student1.getName_inscribed();
        List<Integer> copy_insc = new ArrayList<>(insc);
        List<String> copy_name_insc = new ArrayList<>(name_insc);
        for(int i = 0; i < subject.get().getS_schedule().size(); i++){
            // Obtengo el bloque del ramo
            List<Integer> sch = subject.get().getS_schedule();
            int hor = sch.get(i);
            // Si en ese bloque el estudiante tiene otro ramo, hay tope
            if(student1.getInscribed().get(hor) != 0){
                return -2;
            }
            // Guardo en ese bloque el id del ramo
            copy_insc.set(hor, Math.toIntExact(subject.get().getId_subject()));
            copy_name_insc.set(hor, subject.get().getName());
        }
        // Aumenta la cantidad de ramos tomados
        student1.setEnrolled_subjects(student1.getEnrolled_subjects() + 1);
        student1.setInscribed(copy_insc);
        student1.setName_inscribed(copy_name_insc);
        studentRepository.save(student1);
        ScoresEntity score = new ScoresEntity();
        score.setId_subject(id_subject);
        score.setLevel(subject.get().getLevel());
        score.setRut(student.get().getRut());
        score.setSemester(1); // La inscripción sería para 1-2024
        score.setYear(2024);
        scoresRepository.save(score); // guardo el vinculo estudiante-ramo
        // Agrego un inscrito
        SubjectsEntity subjects =  subject.get();
        subjects.setInscribed(subjects.getInscribed() + 1);
        subjectsService.saveSubject(subjects); // modifico la cantidad de inscritos del ramo
        InfoEntity inf = infoService.getInfoByRutAndSubject(rut, id_subject);
        inf.setIs_inscribed(1); // está inscrito en el ramo
        inf.setStudents_inscribed(inf.getStudents_inscribed() + 1);
        infoService.saveInfo(inf);

        return 1;
    }


    // Para ver si puede inscribir un ramo
    public int canRegisterSubject(String rut, Long id_subject){
        // Para ver si una persona se puede inscribir en un ramo
        Optional<StudentEntity> s = studentService.getStudentByRut(rut);
        Optional<SubjectsEntity> sub = subjectsService.getSubjectsById(id_subject);
        if(s.isPresent() && sub.isPresent()){
            StudentEntity student = s.get();
            SubjectsEntity subject = sub.get();
            if(student.getStay_career() == 0){ // C
                return 0; // No puede tomar ramo si no está permaneciendo en la carrera
            }
            // El número máximo de asignaturas que puede inscribir el o la estudiante
            // corresponde al número de asignaturas de su nivel.
            if(student.getEnrolled_subjects() >= student.getMax_subjects()){
                // Si ya se ha llegado al máximo de ramos inscritos, no se puede
                // inscribir más
                return 0;
            }
            // Un estudiante puede inscribir una asignatura si y solo
            // si ha aprobado todos los prerrequisitos asociados a dicha asignatura
            if(approved_prerequisites(rut, id_subject) == 0){
                // en caso de que no haya aprobado los prerequisitos
                return 0;
            }
            // Las asignaturas podrán ser cursadas para su aprobación hasta en dos oportunidades.
            // La reprobación de una asignatura en segunda oportunidad
            // provoca automáticamente la eliminación del estudiante del programa
            // académico que cursa. No obstante, las asignaturas semestrales de primer
            // nivel podrán cursarse hasta en 3 oportunidades
            if(student.getOpportunities() == 0){
                return 0;
            }

            // Cada asignatura tiene un cupo máximo de estudiantes.
            if(subject.getCapacity() <= subject.getInscribed()){
                // Si los inscritos igualan a la capacidad
                return 0; // No se pueden inscribir más personas
            }


        }
        return 0;
    }

    public List<Object[]> countScoresBySemesterAndYear(String rut) {
        return scoresRepository.subjectsBySemesterAndYear(rut);
    }

    // Para ver si cursó más de 3 ramos por semestre
    // 0 -> no, 1 -> si
    public int subjects_3(String rut){
        List<Object[]> subjectsBySemester = scoresRepository.subjectsBySemesterAndYear(rut);
        for(Object[] s : subjectsBySemester){
            Long count = (Long) s[2];
            //System.out.println(count);
            if(count <= 3){ // si cursó menos o 3 ramos en un semestre, significa que no permanece en la carrera
                return 0;
            }
        }
        return 1;
    }

    public long isApproved(String rut, Long idSubject) {
        return scoresRepository.countByRutAndIdSubjectAndScore(rut, idSubject);
    }

    // Para ver si un estudiante aprobó el prerequisito de un ramo
    // 0 -> no aprobó, 1 -> aprobó
    public int approved_prerequisites(String rut, Long id_subject){
        // Obtengo los prerequisitos del ramo
        List<PrerequisitesEntity> pre = prerequisitesService.getPrerequisitesBySubject(id_subject);
        for(PrerequisitesEntity p : pre){ // Por cada prerequisito, veo si aprobó el estudiante
            //System.out.print(p.getId_prerequisite());
            if(1 > isApproved(rut, p.getId_prerequisite())){ // si reprobó, se retorna 0
                return 0;
            }
        }
        return 1;
    }

    public int getLevel(String rut){
        Optional<StudentEntity> studentOp = studentService.getStudentByRut(rut);
        if(studentOp.isEmpty()){
            return -1;
        }
        int aux_level = 1; // para ver el nivel momentaneo
       if(studentOp.isPresent()){
           StudentEntity student = studentOp.get();
           List<SubjectsEntity> sub = subjectsService.getSubjectsByCareerId(student.getId_career());
           for(SubjectsEntity s : sub){ // Para cada ramo
               if(isApproved(rut, s.getId_subject()) > 0){ // Si aprobó el ramo
                   // System.out.println("Aprueba" + s.getId_subject());
                   aux_level = s.getLevel(); // Se actualiza el nivel con el ramo pasado
               }
               else{ // si no aprobó se queda el nivel antiguo
                   return aux_level;
               }
           }
       }
       return aux_level;
    }

    public void loadAll(String rut){
        Optional<StudentEntity> studentop = studentService.getStudentByRut(rut);
        if(studentop.isPresent()){
            // cargo  nivel
            StudentEntity s = studentop.get();
            if(s.getLevel() == null || s.getStay_career() == null){
                int l = getLevel(s.getRut());
                s.setLevel(l);
                // maximo nivel
                Integer num = Math.toIntExact(subjectsService.getSubjectsByLevelAndCareer(s.getId_career(), s.getLevel()));
                s.setMax_subjects(num);
                // ver si está en la carrera
                if(subjects_3(s.getRut()) == 0){ // si no tomó 3 ramos
                    s.setStay_career(0); // No permanece en la carrera
                }
                else{
                    s.setStay_career(1); // Permanece en la carrera
                }
                // Obtengo estado en torno a reprobaciones
                int num2 = maximum_failure(s);
                s.setOpportunities(num2);
                List<Integer> horary = new ArrayList<>();
                for (int i = 0; i < 55; i++) {
                    horary.add(0);
                }
                s.setInscribed(horary);
                List<String> name_insc = new ArrayList<>();
                for(int i = 0; i < 55; i++){
                    name_insc.add("");
                }
                s.setName_inscribed(name_insc);

                long id_career = s.getId_career();
                Optional<CareerEntity> career_op = careerService.getCareerById(id_career);
                if(career_op.isPresent()){
                    s.setName_career(career_op.get().getName());
                }

                studentService.saveStudent(s);
            }


        }

    }



    public void loadLevels(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity s: studentEntities){
            int l = getLevel(s.getRut());
            s.setLevel(l); // le cambio el nivel
            studentService.saveStudent(s); // guardé el estudiante con nivel actualizado
        }
    }

    public void loadMaxLevels(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity s: studentEntities){
            // Obtengo número de ramos
            Integer num = Math.toIntExact(subjectsService.getSubjectsByLevelAndCareer(s.getId_career(), s.getLevel()));
            s.setMax_subjects(num);
            studentService.saveStudent(s);
        }
    }

    public void stayInTheCareer(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity s: studentEntities){
            // Obtengo número de ramos
            if(subjects_3(s.getRut()) == 0){ // si no tomó 3 ramos
                s.setStay_career(0); // No permanece en la carrera
            }
            else{
                s.setStay_career(1); // Permanece en la carrera
            }
            studentService.saveStudent(s);
        }
    }

    public void load_schedule(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity student : studentEntities){
            List<Integer> horary = new ArrayList<>();
            for (int i = 0; i < 55; i++) {
                horary.add(0);
            }
            student.setInscribed(horary);
            studentService.saveStudent(student);
        }
    }

    public void load_name_inscribed(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity student : studentEntities){
            List<String> name_insc = new ArrayList<>();
            for(int i = 0; i < 55; i++){
                name_insc.add("");
            }
            student.setName_inscribed(name_insc);
            studentService.saveStudent(student);
        }
    }

    public void load_nameCareer(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity student : studentEntities){
            long id_career = student.getId_career();
            Optional<CareerEntity> career_op = careerService.getCareerById(id_career);
            if(career_op.isPresent()){
                student.setName_career(career_op.get().getName());
                studentRepository.save(student);
            }
        }
    }

    // Máximo de reprobación de un estudiante
    public int maximum_failure(StudentEntity student){
        List<Object[]> objects = scoresRepository.countApprovals(student.getRut());
        for(Object[] ob : objects){
            // ob[0]: reprobaciones
            if((long) ob[0] < 2){
                return 1; // no ha reprobado más de 2 veces
            }
            if((long) ob[0] >= 3){ // si ha reprobado 3 o más veces significa eliminación inmediata
                return 0;
            }
            if((long) ob[0] == 2){ // si ha reprobado 2 veces un ramo, debe verse si es de primer nivel
                long id_s = (long) ob[1];
                Optional<SubjectsEntity> subject = subjectsService.getSubjectsById(id_s);
                if(subject.isPresent()){
                    if(subject.get().getLevel() != 1){
                        // si no es de primer nivel, no puede reprobarse 2 veces
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    public void load_nFailure(){
        List<StudentEntity> studentEntities = studentService.getAllStudents();
        for(StudentEntity s: studentEntities){
            // Obtengo estado en torno a reprobaciones
            int num = maximum_failure(s);
            s.setOpportunities(num);
            studentService.saveStudent(s);
        }
    }








}
