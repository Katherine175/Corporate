package com.luxury.eventoacceso.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxury.common.enums.TipoEventoAcceso;
import com.luxury.eventoacceso.dto.EventoAccesoResponse;
import com.luxury.eventoacceso.model.EventoAcceso;
import com.luxury.eventoacceso.repository.EventoAccesoRepository;
import com.luxury.usuario.model.Usuario;

@Service
public class EventoAccesoService {

	@Autowired
	private EventoAccesoRepository eventoAccesoRepository;

	public void registrar(Usuario usuario, String emailIntentado, TipoEventoAcceso tipoEvento, String descripcion, String ip) {
		EventoAcceso evento = new EventoAcceso();
		evento.setUsuario(usuario);
		evento.setEmailIntentado(emailIntentado);
		evento.setTipoEvento(tipoEvento);
		evento.setDescripcion(descripcion);
		evento.setIp(ip);
		eventoAccesoRepository.save(evento);
	}

	public List<EventoAccesoResponse> listar() {
		return eventoAccesoRepository.findAll().stream().map(EventoAccesoResponse::from).toList();
	}
}
